package ru.kata.spring.boot_security.demo.repository;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Component
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Role> findAll() {
        List<Role> rolesList = em.createQuery("from Role", Role.class).getResultList();
        return rolesList == null ? new ArrayList<>() : rolesList;
    }

    @Override
    public Role getById(long id) {
        return em.find(Role.class, id);
    }
}
