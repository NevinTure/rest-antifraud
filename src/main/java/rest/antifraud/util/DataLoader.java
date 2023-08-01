package rest.antifraud.util;

import rest.antifraud.models.Maximum;
import rest.antifraud.models.Role;
import rest.antifraud.repositories.MaximumRepository;
import rest.antifraud.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataLoader {

    private final RoleRepository roleRepository;
    private final MaximumRepository maximumRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository, MaximumRepository maximumRepository) {
        this.roleRepository = roleRepository;
        this.maximumRepository = maximumRepository;
        createRoles();
    }

    private void createRoles() {
        if(!roleRepository.existsByName("ROLE_ADMINISTRATOR")) {
            roleRepository.save(new Role("ROLE_ADMINISTRATOR"));
        }
        if(!roleRepository.existsByName("ROLE_MERCHANT")) {
            roleRepository.save(new Role("ROLE_MERCHANT"));
        }
        if(!roleRepository.existsByName("ROLE_SUPPORT")) {
            roleRepository.save(new Role("ROLE_SUPPORT"));
        }
        if(!maximumRepository.existsById("allowed")) {
            maximumRepository.save(new Maximum("allowed", 200));
        }
        if(!maximumRepository.existsById("manual")) {
            maximumRepository.save(new Maximum("manual", 1500));
        }
    }
}
