package rest.antifraud.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionResponseBuilder {

    private TransactionStatus status;
    private final Map<String, TransactionStatus> causes;

    public TransactionResponseBuilder() {
        causes = new LinkedHashMap<>();
        causes.put("amount", TransactionStatus.ALLOWED);
        causes.put("card-number", TransactionStatus.ALLOWED);
        causes.put("ip", TransactionStatus.ALLOWED);
        causes.put("ip-correlation", TransactionStatus.ALLOWED);
        causes.put("region-correlation", TransactionStatus.ALLOWED);
    }

    public void setStatus() {
        if (causes.containsValue(TransactionStatus.PROHIBITED)) {
            status = TransactionStatus.PROHIBITED;
        } else if (causes.containsValue(TransactionStatus.MANUAL_PROCESSING)) {
            status = TransactionStatus.MANUAL_PROCESSING;
        } else {
            status = TransactionStatus.ALLOWED;
        }
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setAmountCause(TransactionStatus status) {
        causes.replace("amount", status);
        setStatus();
    }

    public Map<String, TransactionStatus> getCauses() {
        return causes;
    }

    public void setNumberCause(TransactionStatus status) {
        causes.replace("card-number", status);
        setStatus();
    }

    public void setIpCause(TransactionStatus status) {
        causes.replace("ip", status);
        setStatus();
    }

    public void setIpCorrelationCause(TransactionStatus status) {
        causes.replace("ip-correlation", status);
        setStatus();
    }

    public void setRegionCorrelationCause(TransactionStatus status) {
        causes.replace("region-correlation", status);
        setStatus();
    }

    public TransactionResponse build() {
        if (status.equals(TransactionStatus.ALLOWED)) {
            return new TransactionResponse(status, "none");
        } else {
            return new TransactionResponse(status, buildInfo());
        }
    }

    private String buildInfo() {
        return causes
                .keySet()
                .stream()
                .filter(v -> causes.get(v).equals(status))
                .sorted()
                .collect(Collectors.joining(", "));

    }
}
