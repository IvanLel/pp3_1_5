package ru.kata.spring.boot_security.demo.repository;


import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserDao {

    void save(User user);

    List<User> findAll();

    User findById(long id);

    User findByUsername(String username);

    void deleteById(long id);

    boolean checkUsernameExistence(String username);

    boolean checkEmailExistence(String email);
}
