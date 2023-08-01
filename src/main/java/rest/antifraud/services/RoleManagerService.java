package rest.antifraud.services;

import rest.antifraud.exceptions.ManageRoleException;
import rest.antifraud.models.Role;
import rest.antifraud.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class RoleManagerService {

    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public RoleManagerService(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    public void changeRole(User user, String roleStr) {
        Role role = roleService.getByName("ROLE_" + roleStr);
        if(Objects.equals(user.getRole(), role)) {
            throw new ManageRoleException("Role already provided to this user!");
        } else {
            user.setRole(role);
            userService.update(user);
        }
    }
}
