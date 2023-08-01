package rest.antifraud.dto;

import rest.antifraud.annotations.IpAddressConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IpAddressDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long id;

    @IpAddressConstraint
    @JsonProperty("ip")
    private String address;
}
