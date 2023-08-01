package rest.antifraud.services;

import rest.antifraud.models.Role;
import rest.antifraud.models.User;
import rest.antifraud.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder encoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsernameIgnoreCase(username);
        if(userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UsernameNotFoundException("User not found!");
        }
    }

    public void save(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        if(userRepository.count() == 0) {
            user.setRole(roleService.getByName("ROLE_ADMINISTRATOR"));
            user.setAccountNonLocked(true);
        } else {
            user.setRole(roleService.getByName("ROLE_MERCHANT"));
            user.setAccountNonLocked(false);
        }
        userRepository.save(user);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public boolean isExistsByUsername(String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    public List<User> getAll() {
        return userRepository.findAllOrderById();
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public void lock(User user) {
        Role adminRole = roleService.getByName("ROLE_ADMINISTRATOR");
        if(Objects.equals(adminRole, user.getRole())) {
            throw new LockedException("Cannot lock ADMINISTRATOR");
        } else {
            user.setAccountNonLocked(false);
            update(user);
        }
    }

    public void unlock(User user) {
        user.setAccountNonLocked(true);
        update(user);
    }
}
