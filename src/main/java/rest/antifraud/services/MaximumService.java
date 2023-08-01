package rest.antifraud.services;

import rest.antifraud.models.Maximum;
import rest.antifraud.models.Transaction;
import rest.antifraud.repositories.MaximumRepository;
import org.springframework.stereotype.Service;

@Service
public class MaximumService {

    private final MaximumRepository maximumRepository;


    public MaximumService(MaximumRepository maximumRepository) {
        this.maximumRepository = maximumRepository;
    }

    public Maximum getAllowedMaximum() {
        return maximumRepository.getReferenceById("allowed");
    }

    public Maximum getManualMaximum() {
        return maximumRepository.getReferenceById("manual");
    }

    public void increaseAllowedMaximum(Transaction transaction) {
        Maximum allowedMaximum = getAllowedMaximum();
        long currentLimit = allowedMaximum.getMaximumValue();
        long valueFromTransaction = transaction.getAmount();
        long newLimit = (long) Math.ceil(0.8 * currentLimit + 0.2 * valueFromTransaction);
        allowedMaximum.setMaximumValue(newLimit);
        save(allowedMaximum);
    }

    public void decreaseAllowedMaximum(Transaction transaction) {
        Maximum allowedMaximum = getAllowedMaximum();
        long currentLimit = allowedMaximum.getMaximumValue();
        long valueFromTransaction = transaction.getAmount();
        long newLimit = (long) Math.ceil(0.8 * currentLimit - 0.2 * valueFromTransaction);
        allowedMaximum.setMaximumValue(newLimit);
        save(allowedMaximum);
    }

    public void increaseManualMaximum(Transaction transaction) {
        Maximum allowedMaximum = getManualMaximum();
        long currentLimit = allowedMaximum.getMaximumValue();
        long valueFromTransaction = transaction.getAmount();
        long newLimit = (long) Math.ceil(0.8 * currentLimit + 0.2 * valueFromTransaction);
        allowedMaximum.setMaximumValue(newLimit);
        save(allowedMaximum);
    }

    public void decreaseManualMaximum(Transaction transaction) {
        Maximum allowedMaximum = getManualMaximum();
        long currentLimit = allowedMaximum.getMaximumValue();
        long valueFromTransaction = transaction.getAmount();
        long newLimit = (long) Math.ceil(0.8 * currentLimit - 0.2 * valueFromTransaction);
        allowedMaximum.setMaximumValue(newLimit);
        save(allowedMaximum);
    }

    private void save(Maximum maximum) {
        maximumRepository.save(maximum);
    }
}
