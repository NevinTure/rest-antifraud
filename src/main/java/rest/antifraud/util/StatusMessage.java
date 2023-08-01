package rest.antifraud.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatusMessage {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String username;
    private String status;

    public StatusMessage(String status) {
        this.status = status;
    }
}
