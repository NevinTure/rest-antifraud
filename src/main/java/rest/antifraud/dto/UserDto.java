package rest.antifraud.dto;

import rest.antifraud.models.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private long id;

    @NotEmpty
    private String name;

    @NotEmpty
    private String username;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty
    private String password;

    private String role;

    @JsonProperty("role")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public void setRole(String role) {
        this.role = role.replace("ROLE_", "");
    }
}
