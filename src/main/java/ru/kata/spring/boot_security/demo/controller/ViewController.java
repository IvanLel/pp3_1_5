package ru.kata.spring.boot_security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
public class ViewController {

    private UserService userService;

    public ViewController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/admin/adminPanel")
    public String adminPanel(Model model, Principal principal) {
        model.addAttribute("loggedInUser",userService.findByUsername(principal.getName()));
        return "adminPage";
    }

    @RequestMapping("/user")
    public String userPage(Model model, Principal principal) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        return "userPage";
    }
}
