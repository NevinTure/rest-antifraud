package rest.antifraud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.LuhnCheck;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CardDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long id;

    @LuhnCheck
    private String number;

}
