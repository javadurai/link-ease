package com.linkease.controller;

import com.linkease.domain.User;
import com.linkease.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String homePage() {
        return "redirect:/links"; // This should match the name of your registration HTML file
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration"; // This should match the name of your registration HTML file
    }

    @PostMapping("/register")
    public String registerUser(User user, Model model) {
        // Add your registration logic (e.g., save user, handle errors)
        userService.createUser(user);
        return "redirect:/login"; // Redirect to login after successful registration
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(value = "logout", required = false) String logout, Model model) {
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been successfully logged out.");
        }
        return "login"; // This should match the name of your login HTML file
    }
}
