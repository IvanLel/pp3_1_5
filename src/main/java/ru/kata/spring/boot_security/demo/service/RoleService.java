package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.repository.RoleDao;

import java.util.List;

@Service
@Transactional
public class RoleService {
    private List<Role> allRoles;

    private RoleDao roleDao;

    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }


    public List<Role> getAllRoles() {
        if (allRoles == null) {
              allRoles = roleDao.findAll();
        }
        return allRoles;
    }

    public Role getById(long id) {
        return roleDao.getById(id);
    }
}
