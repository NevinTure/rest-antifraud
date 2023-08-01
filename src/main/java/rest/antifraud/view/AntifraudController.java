package rest.antifraud.view;

import rest.antifraud.annotations.IpAddressConstraint;
import rest.antifraud.dto.CardDto;
import rest.antifraud.dto.IpAddressDto;
import rest.antifraud.dto.TransactionDto;
import rest.antifraud.models.Card;
import rest.antifraud.models.IpAddress;
import rest.antifraud.models.Transaction;
import rest.antifraud.services.CardService;
import rest.antifraud.services.IpAddressService;
import rest.antifraud.services.TransactionService;
import rest.antifraud.util.FeedbackRequest;
import rest.antifraud.util.StatusMessage;
import rest.antifraud.util.TransactionResponse;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.LuhnCheck;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/antifraud")
@Validated
public class AntifraudController {

    private final IpAddressService ipAddressService;
    private final CardService cardService;
    private final TransactionService transactionService;
    private final ModelMapper mapper;

    @Autowired
    public AntifraudController(IpAddressService ipAddressService, CardService cardService, TransactionService transactionService, ModelMapper mapper) {
        this.ipAddressService = ipAddressService;
        this.cardService = cardService;
        this.transactionService = transactionService;
        this.mapper = mapper;
    }

    @PostMapping("/transaction")
    public ResponseEntity<TransactionResponse> processTransaction(@Valid @RequestBody TransactionDto tranDto) {
        Transaction transaction = mapper.map(tranDto, Transaction.class);
        TransactionResponse result = transactionService.checkTransactionAndGetResponse(transaction);
        transactionService.save(transaction);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/suspicious-ip")
    public ResponseEntity<Object> addSuspiciousIp(@Valid @RequestBody IpAddressDto ipAddressDto) {
        if (ipAddressService.existsByAddress(ipAddressDto.getAddress())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            IpAddress address = mapper.map(ipAddressDto, IpAddress.class);
            ipAddressService.save(address);
            ipAddressDto.setId(address.getId());
            return new ResponseEntity<>(ipAddressDto, HttpStatus.OK);
        }
    }

    @DeleteMapping("/suspicious-ip/{ip}")
    public ResponseEntity<Object> deleteIpAddress(
            @IpAddressConstraint @PathVariable(name = "ip") String address) {
        if(!ipAddressService.existsByAddress(address)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            ipAddressService.deleteByAddress(address);
            return new ResponseEntity<>(
                    new StatusMessage("IP %s successfully removed!".formatted(address)),
                    HttpStatus.OK);
        }
    }

    @GetMapping("/suspicious-ip")
    public ResponseEntity<List<IpAddressDto>> getIpAddresses() {
        List<IpAddressDto> ipAddressDtos = ipAddressService
                .getAll()
                .stream()
                .map(v -> mapper.map(v, IpAddressDto.class))
                .toList();

        return new ResponseEntity<>(ipAddressDtos, HttpStatus.OK);
    }

    @PostMapping("/stolencard")
    public ResponseEntity<Object> addStolenCard(@Valid @RequestBody CardDto cardDto) {
        if (cardService.existsByNumber(cardDto.getNumber())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            Card card = mapper.map(cardDto, Card.class);
            cardService.save(card);
            cardDto.setId(card.getId());
            return new ResponseEntity<>(cardDto, HttpStatus.OK);
        }
    }

    @DeleteMapping("/stolencard/{number}")
    public ResponseEntity<Object> deleteCard(
            @LuhnCheck @PathVariable(name = "number") String number) {
        if (!cardService.existsByNumber(number)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            cardService.deleteByNumber(number);
            return new ResponseEntity<>(
                    new StatusMessage("Card %s successfully removed!".formatted(number)),
                    HttpStatus.OK);
        }
    }

    @GetMapping("/stolencard")
    public ResponseEntity<List<CardDto>> getCards() {
        List<CardDto> cardDtos = cardService
                .getAll()
                .stream()
                .map(v -> mapper.map(v, CardDto.class))
                .toList();

        return new ResponseEntity<>(cardDtos, HttpStatus.OK);
    }

    @PutMapping("/transaction")
    public ResponseEntity<Object> addFeedback(@RequestBody FeedbackRequest request) {
        Optional<Transaction> transactionOptional = transactionService.getById(request.getTransactionId());
        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();
            if (transaction.getFeedback() != null) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            if (!transactionService.addFeedback(request.getFeedback(), transaction)) {
                return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
            }
            transactionService.save(transaction);
            TransactionDto transactionDto = mapper.map(transaction, TransactionDto.class);
            return new ResponseEntity<>(transactionDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionDto>> getTransactionsHistory() {
        List<TransactionDto> transactionDtos = transactionService
                .getAll()
                .stream()
                .map(v -> {
                    TransactionDto dto  = mapper.map(v, TransactionDto.class);
                    if (dto.getFeedback() == null) {
                        dto.setFeedback("");
                    }
                    return dto;
                })
                .toList();

        return new ResponseEntity<>(transactionDtos, HttpStatus.OK);
    }

    @GetMapping("/history/{number}")
    public ResponseEntity<Object> getTransactionsByNumber(
            @CreditCardNumber @PathVariable(name = "number") String number) {

        List<TransactionDto> transactionDtos = transactionService
                .getAllByNumber(number)
                .stream()
                .map(v -> {
                    TransactionDto dto  = mapper.map(v, TransactionDto.class);
                    if (dto.getFeedback() == null) {
                        dto.setFeedback("");
                    }
                    return dto;
                })
                .toList();

        if (transactionDtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(transactionDtos, HttpStatus.OK);
        }
    }
}
