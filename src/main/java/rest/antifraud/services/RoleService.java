package rest.antifraud.services;

import rest.antifraud.models.Role;
import rest.antifraud.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(noRollbackFor = Exception.class)
    public Role getByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }
}
