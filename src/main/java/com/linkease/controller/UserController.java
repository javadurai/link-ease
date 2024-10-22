package com.linkease.controller;

import com.linkease.domain.Role;
import com.linkease.domain.User;
import com.linkease.repository.RoleRepository;
import com.linkease.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // List users with pagination
    @GetMapping
    public String getAllUsers(
            @RequestParam Optional<String> username,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage;

        if (username.isPresent()) {
            userPage = userRepository.findByUsernameContaining(username.get(), pageable);
        } else {
            userPage = userRepository.findAll(pageable);
        }

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageTitle", "User List");
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());

        return "users/list";
    }

    // Show form to create a new user
    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new User());
        return "users/create";
    }

    // Create a new user
    @PostMapping("/create")
    public String createUser(@ModelAttribute User user) {
        userRepository.save(user);
        return "redirect:/users";
    }

    // Show form to edit an existing user
    @GetMapping("/edit/{id}")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "users/edit";
    }

    // Update an existing user
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        user.setFullName(userDetails.getFullName());
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        //user.setPassword(userDetails.getPassword());  // Should be encoded in practice
        userRepository.save(user);
        return "redirect:/users";
    }

    // Delete a user
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return "redirect:/users";
    }

    // Show form to assign roles to user
    @GetMapping("/assign-roles/{id}")
    public String showAssignRolesForm(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAll());  // Fetch roles from the role service
        return "users/assign-roles"; // You will create this HTML page
    }

    // Handle role assignment to the user
    @PostMapping("/assign-roles/{id}")
    public String assignRoles(@PathVariable Long id, @RequestParam List<Long> roleIds) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // Assign roles to the user
        Set<Role> roles = roleIds.stream().map(r -> {
            Optional<Role> role = roleRepository.findById(r);
            return role.orElse(null);
        }).filter(Objects::nonNull).collect(Collectors.toSet());
        user.setRoles(roles);
        userRepository.save(user);
        return "redirect:/users"; // Redirect back to the user list
    }

    // Handle password reset
    @GetMapping("/reset-password/{id}")
    public String resetPassword(@PathVariable Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));

        // Generate a new random password (you can use a different method if needed)
        String newPassword = RandomStringUtils.randomAlphanumeric(10); // Generates a 10-character password

        // You should encode the password before saving it
        user.setPassword(passwordEncoder.encode(newPassword)); // In practice, encode the password
        userRepository.save(user);

        model.addAttribute("message", "Password reset successfully. New password: " + newPassword);
        return "redirect:/users"; // Redirect to the user list, or show the new password if necessary
    }
}
