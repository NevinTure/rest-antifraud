package rest.antifraud.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FeedbackRequest {

    private long transactionId;

    private TransactionStatus feedback;
}
