package ru.kata.spring.boot_security.demo.repository;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<User> findAll() {
        List<User> userList = em.createQuery("from User", User.class).getResultList();

        return userList == null ? new ArrayList<>() : userList;
    }

    @Override
    public User findById(long id) {
        return em.find(User.class, id);
    }

    @Override
    public void save(User user) {
        em.persist(user);
    }

    @Override
    public void deleteById(long id) {
        em.remove(em.find(User.class, id));
    }

    @Override
    public User findByUsername(String username) {
        TypedQuery<User> query = em.createQuery("from User where username =: username", User.class);
        query.setParameter("username", username);
        User user = null;
        try {
            user = query.getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean checkUsernameExistence(String username) throws NoResultException {
        return findByUsername(username) != null;
    }

    public boolean checkEmailExistence(String email) throws NoResultException {
        TypedQuery<User> query = em.createQuery("from User where email =: email", User.class);
        query.setParameter("email", email);
        try {
            query.getSingleResult();
        } catch (NoResultException e) {
            return false;
        }
        return true;
    }
}