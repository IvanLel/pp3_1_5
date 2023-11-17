package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class RESTControllerAdmin {

    private UserService userService;

    private RoleService roleService;

    public RESTControllerAdmin(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<User> showAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/roles")
    public List<Role> getAllRoles() {return roleService.getAllRoles(); }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable("id") long id) {
        return userService.getById(id);
    }

    @PostMapping("/users")
    public ResponseEntity<?> addNewUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        user.setRoles(manageRoles(user.getRoles()));
        userService.addNewUser(user);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") long id,
                                        @Valid @RequestBody User user,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        user.setRoles(manageRoles(user.getRoles()));
        userService.updateUser(user, id);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public User deleteUser(@PathVariable("id") long id) {
        return userService.deleteById(id);
    }

    private Set<Role> manageRoles(Set<Role> roles) {
        return roles.stream()
                .map(role -> roleService.getById(role.getId()))
                .collect(Collectors.toSet());
    }
}
