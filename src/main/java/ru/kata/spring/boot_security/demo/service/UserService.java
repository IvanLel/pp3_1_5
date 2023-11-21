package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserDao;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService implements UserDetailsService {

    private UserDao userDao;

    @Autowired
    public void setUserRepository(UserDao userDao) {
        this.userDao = userDao;
    }


    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User - %s  not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), getAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    @Transactional(readOnly = true)
    public User getById(long id) {
        return userDao.findById(id);
    }

    public User deleteById(long id) {
        User user = getById(id);
        userDao.deleteById(id);
        return user;
    }

    public void updateUser(User user, long id) {
        User userFromDB = getById(id);

        if (StringUtils.hasLength(user.getPassword())) {
            userFromDB.setPassword(passEncoder().encode(user.getPassword()));
        }

        userFromDB.setUsername(user.getUsername());
        userFromDB.setEmail(user.getEmail());
        userFromDB.setAge(user.getAge());
        userFromDB.setRoles(user.getRoles());

        userDao.save(userFromDB);
    }

    public void addNewUser(User user) {
        user.setPassword(passEncoder().encode(user.getPassword()));
        userDao.save(user);
    }

    public boolean checkUsernameExistence(String username) {
        return userDao.checkUsernameExistence(username);
    }

    public boolean checkEmailExistence(String email) {
        return userDao.checkEmailExistence(email);
    }

    private BCryptPasswordEncoder passEncoder() {
        return new BCryptPasswordEncoder();
    }
}
