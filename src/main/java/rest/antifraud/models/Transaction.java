package rest.antifraud.models;

import rest.antifraud.util.Region;
import rest.antifraud.util.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private long id;

    @Column(name = "amount")
    private long amount;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "card_number")
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "region")
    private Region region;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "result")
    private TransactionStatus result;

    @Column(name = "feedback")
    private TransactionStatus feedback;
}
