package rest.antifraud.util;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoleManagerRequest {

    private String username;

    @Pattern(regexp = "(SUPPORT)|(MERCHANT)")
    private String role;

}
