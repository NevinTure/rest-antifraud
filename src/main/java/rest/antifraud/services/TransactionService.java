package rest.antifraud.services;

import rest.antifraud.models.Transaction;
import rest.antifraud.repositories.TransactionRepository;
import rest.antifraud.util.TransactionResponse;
import rest.antifraud.util.TransactionResponseBuilder;
import rest.antifraud.util.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;

@Slf4j
@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final IpAddressService ipAddressService;

    private final MaximumService maximumService;
    private final CardService cardService;

    public TransactionService(TransactionRepository transactionRepository, IpAddressService ipAddressService, MaximumService maximumService, CardService cardService) {
        this.transactionRepository = transactionRepository;
        this.ipAddressService = ipAddressService;
        this.maximumService = maximumService;
        this.cardService = cardService;
    }

    public void save(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public Optional<Transaction> getById(long id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getAllByNumber(String number) {
        return transactionRepository.findAllByCardNumber(number);
    }

    public boolean addFeedback(TransactionStatus feedback, Transaction transaction) {
        if (transaction.getResult().equals(feedback)) {
            return false;
        }
        processFeedback(transaction, feedback);
        transaction.setFeedback(feedback);
        return true;
    }

    public void processFeedback(Transaction transaction, TransactionStatus feedback) {
        TransactionStatus validity = transaction.getResult();
        if (validity.equals(TransactionStatus.ALLOWED)) {
            if (feedback.equals(TransactionStatus.MANUAL_PROCESSING)) {
                maximumService.decreaseAllowedMaximum(transaction);
            } else {
                maximumService.decreaseAllowedMaximum(transaction);
                maximumService.decreaseManualMaximum(transaction);
            }
        } else if (validity.equals(TransactionStatus.MANUAL_PROCESSING)) {
            if (feedback.equals(TransactionStatus.ALLOWED)) {
                maximumService.increaseAllowedMaximum(transaction);
            } else {
                maximumService.decreaseManualMaximum(transaction);
            }
        } else {
            if (feedback.equals(TransactionStatus.ALLOWED)) {
                maximumService.increaseAllowedMaximum(transaction);
                maximumService.increaseManualMaximum(transaction);
            } else {
                maximumService.increaseManualMaximum(transaction);
            }
        }
    }

    public TransactionResponse checkTransactionAndGetResponse(Transaction transaction) {
        TransactionResponseBuilder responseBuilder = new TransactionResponseBuilder();

        responseBuilder.setAmountCause(checkAmount(transaction));
        responseBuilder.setNumberCause(checkStolenCard(transaction));
        responseBuilder.setIpCause(checkSuspiciousIpAddress(transaction));
        responseBuilder.setIpCorrelationCause(checkIpCorrelationByNumber(transaction));
        responseBuilder.setRegionCorrelationCause(checkRegionCorrelationByNumber(transaction));
        transaction.setResult(responseBuilder.getStatus());
        return responseBuilder.build();
    }


    private TransactionStatus checkIpCorrelationByNumber(Transaction transaction) {
        LocalDateTime transactionDateTime = transaction.getDateTime();
        List<String> transactionIpsInLastHour =
                transactionRepository
                        .findDistinctIpByDateTimeBetween(
                                transaction.getCardNumber(),
                                transactionDateTime.minusHours(1L),
                                transactionDateTime
                        );
        BiPredicate<Transaction, String> checkIp = (v1, v2) -> !v1.getIpAddress().equals(v2);
        return checkCorrelationByNumber(
                transaction, transactionIpsInLastHour, checkIp
        );
    }

    private TransactionStatus checkRegionCorrelationByNumber(Transaction transaction) {
        LocalDateTime transactionDateTime = transaction.getDateTime();
        List<String> transactionRegionsInLastHour =
                transactionRepository
                        .findDistinctRegionByDateTimeBetween(
                                transaction.getCardNumber(),
                                transactionDateTime.minusHours(1L),
                                transactionDateTime
                        );
        BiPredicate<Transaction, String> checkRegion = (v1, v2) -> !v1.getRegion().toString().equals(v2);
        return checkCorrelationByNumber(
                transaction, transactionRegionsInLastHour, checkRegion
        );
    }

    private TransactionStatus checkAmount(Transaction transaction) {
        long amount = transaction.getAmount();
        if (amount <= maximumService.getAllowedMaximum().getMaximumValue()) {
            return TransactionStatus.ALLOWED;
        } else if (amount <= maximumService.getManualMaximum().getMaximumValue()) {
            return  TransactionStatus.MANUAL_PROCESSING;
        } else {
            return TransactionStatus.PROHIBITED;
        }

    }

    private TransactionStatus checkCorrelationByNumber (
            Transaction transaction,
            List<String> valuesInLastHour,
            BiPredicate<Transaction, String> checker) {

        int transactionCounter = 0;
        for (String value : valuesInLastHour) {
            if (checker.test(transaction, value)) {
                transactionCounter++;
                if(transactionCounter > 3) {
                    break;
                }
            }
        }
        return transactionCounter > 2 ? TransactionStatus.PROHIBITED
                : transactionCounter > 1 ? TransactionStatus.MANUAL_PROCESSING
                : TransactionStatus.ALLOWED;
    }

    private TransactionStatus checkSuspiciousIpAddress(Transaction transaction) {
        if (ipAddressService.existsByAddress(transaction.getIpAddress())) {
            return TransactionStatus.PROHIBITED;
        } else {
            return TransactionStatus.ALLOWED;
        }
    }

    private TransactionStatus checkStolenCard(Transaction transaction) {
        if (cardService.existsByNumber(transaction.getCardNumber())) {
            return TransactionStatus.PROHIBITED;
        } else {
            return TransactionStatus.ALLOWED;
        }
    }
}
