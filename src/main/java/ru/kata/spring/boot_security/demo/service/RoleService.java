package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;

import java.util.List;

@Service
public class RoleService {
    private List<Role> allRoles;

    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        if (allRoles == null) {
              allRoles = roleRepository.findAll();
        }
        return allRoles;
    }

    public Role getById(long id) {
        return roleRepository.getById(id);
    }
}
