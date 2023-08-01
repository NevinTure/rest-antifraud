package rest.antifraud.dto;

import rest.antifraud.annotations.IpAddressConstraint;
import rest.antifraud.util.Region;
import rest.antifraud.util.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.LuhnCheck;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long transactionId;


    @Min(1)
    private long amount;

    @NotBlank
    @IpAddressConstraint
    @JsonProperty("ip")
    private String ipAddress;

    @LuhnCheck
    @NotBlank
    @JsonProperty("number")
    private String cardNumber;

    private Region region;

    @JsonProperty("date")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime dateTime;

    private String result;

    private String feedback;
}
