package rest.antifraud.view;

import rest.antifraud.dto.UserDto;
import rest.antifraud.models.User;
import rest.antifraud.services.RoleManagerService;
import rest.antifraud.services.UserService;
import rest.antifraud.util.LockManagerRequest;
import rest.antifraud.util.RoleManagerRequest;
import rest.antifraud.util.StatusMessage;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthenticationController {

    private final UserService userService;

    private final RoleManagerService roleManagerService;
    private final ModelMapper mapper;


    @Autowired
    public AuthenticationController(UserService userService, RoleManagerService roleManagerService, ModelMapper mapper) {
        this.userService = userService;
        this.roleManagerService = roleManagerService;
        this.mapper = mapper;
    }

    @PostMapping("/user")
    public ResponseEntity<Object> register(@Valid @RequestBody UserDto userDto) {
        if(userService.isExistsByUsername(userDto.getUsername())) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        } else {
            User user = mapper.map(userDto, User.class);
            userService.save(user);
            userDto = mapper.map(user, UserDto.class);
            userDto.setPassword(null);
            return new ResponseEntity<>(userDto, HttpStatus.CREATED);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> getUserList() {
        List<UserDto> userDtos = userService
                .getAll()
                .stream()
                .map(v -> {
                    UserDto userDTO = mapper.map(v, UserDto.class);
                    userDTO.setPassword(null);
                    return userDTO;
                })
                .toList();

        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @DeleteMapping("/user/{username}")
    public ResponseEntity<Object> deleteUser(@PathVariable(name = "username") String username) {
        Optional<User> userOptional = userService.getByUsername(username);
        if (userOptional.isPresent()) {
            username = userOptional.get().getUsername();
            userService.delete(userOptional.get());
            return new ResponseEntity<>(
                    new StatusMessage(username, "Deleted successfully!"),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/role")
    public ResponseEntity<Object> manageUserRole(@Valid @RequestBody RoleManagerRequest request) {
        Optional<User> userOptional = userService.getByUsername(request.getUsername());
        if(userOptional.isPresent()) {
            roleManagerService.changeRole(userOptional.get(), request.getRole());
            UserDto userDto = mapper.map(userOptional.get(), UserDto.class);
            userDto.setRole(request.getRole());
            userDto.setPassword(null);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/access")
    public ResponseEntity<Object> manageLock(@RequestBody LockManagerRequest request) {
        Optional<User> userOptional = userService.getByUsername(request.getUsername());
        if(userOptional.isPresent()) {
            switch (request.getOperation()) {
                case "LOCK" -> userService.lock(userOptional.get());
                case "UNLOCK" -> userService.unlock(userOptional.get());
            }
            return new ResponseEntity<>(new StatusMessage(
                    String.format("User %s %s",
                            request.getUsername(),
                            request.getOperation().toLowerCase() + "ed!")),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
