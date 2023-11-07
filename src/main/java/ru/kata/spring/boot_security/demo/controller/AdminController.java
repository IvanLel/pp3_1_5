package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final String REDIRECT_TO_ADMIN_PAGE = "redirect:/admin";

    private UserService userService;

    private RoleRepository roleRepository;


    @Autowired
    public void setUserService(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    private void addAttributesForAdminPanel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.findByUsername(authentication.getName());
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("allRoles", roleRepository.findAll());
    }


    @GetMapping
    public String adminPage(Model model) {
        addAttributesForAdminPanel(model);
        model.addAttribute("user", new User());
        model.addAttribute("users", userService.getAllUsers());
        return "adminPage";
    }

    @RequestMapping("/findUser")
    @ResponseBody
    public User getUser(long id) {
        return userService.getById(id);
    }

    @PostMapping("/updateUser")
    public String save(@ModelAttribute("user") @Valid User user,
                           BindingResult bindingResult,
                           Model model) {

        BeanPropertyBindingResult filteredBindingResult = new BeanPropertyBindingResult(user, "user");
        for (ObjectError error : bindingResult.getAllErrors()) {
            if (error instanceof FieldError &&
                    "password".equals(((FieldError) error).getField()) &&
                    user.getPassword().isEmpty()) {
                continue;
            }
            filteredBindingResult.addError(error);
        }

        bindingResult = filteredBindingResult;

        if (user.getPassword().isEmpty()) {
            User userFromDB = userService.getById(user.getId());
            user.setPassword(userFromDB.getPassword());
        }

        if (bindingResult.hasErrors()) {
            addAttributesForAdminPanel(model);
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("user", user);
            return "adminPage";
        }
        userService.updateUser(user, user.getId());

        return REDIRECT_TO_ADMIN_PAGE;
    }

    @GetMapping("/addUserTab")
    public String addUserTab(Model model) {
        model.addAttribute("newUser", new User());
        addAttributesForAdminPanel(model);
        return "addUserTab";
    }

    @PostMapping("/addUser")
    public String addNewUser(@ModelAttribute("newUser") @Valid User user, BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("newUser", user);
            addAttributesForAdminPanel(model);
            return "addUserTab";
        }
        userService.addNewUser(user);

        return REDIRECT_TO_ADMIN_PAGE;
    }

    @RequestMapping("/delete")
    public String deleteUser(@ModelAttribute("user") User user) {
        userService.deleteById(user.getId());
        return REDIRECT_TO_ADMIN_PAGE;
    }

}
