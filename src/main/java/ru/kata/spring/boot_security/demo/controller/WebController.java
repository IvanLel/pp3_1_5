package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class WebController {

    private final String REDIRECT_TO_ADMIN_PAGE = "redirect:/admin";

    private final String EDIT_PAGE = "editPage";

    private UserService userService;

    private RoleRepository roleRepository;

    @Autowired
    public void setUserService(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/user")
    public String userPage(Principal principal, Model model) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        return "userPage";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "adminPage";
    }

    @RequestMapping("/admin/delete")
    public String deleteUser(@RequestParam("id") long id) {
        userService.deleteById(id);
        return REDIRECT_TO_ADMIN_PAGE;
    }

    @RequestMapping("/admin/edit")
    public String editUser(Model model, @RequestParam("id") long id) {
        if (id == 0) {
            model.addAttribute("user", new User());
        } else {
            model.addAttribute("user",userService.getById(id));
        }

        model.addAttribute("allRoles", roleRepository.findAll());

        return EDIT_PAGE;
    }

    @PostMapping("/admin/edit/")
    public String editPage(@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult,
                           @RequestParam("id") long id,
                           Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            return EDIT_PAGE;
        }
        if (id == 0) {
            userService.addNewUser(user);
        } else {
            userService.updateUser(user, user.getId());
        }

        return REDIRECT_TO_ADMIN_PAGE;
    }

    @RequestMapping("admin/addUser")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return EDIT_PAGE;
    }

}
