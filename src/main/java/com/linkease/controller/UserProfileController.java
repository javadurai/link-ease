package com.linkease.controller;

import com.linkease.domain.Link;
import com.linkease.domain.User;
import com.linkease.repository.LinkRepository;
import com.linkease.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserRepository userRepository;
    private final LinkRepository linkRepository;
    private final PasswordEncoder passwordEncoder;

    // Display profile of the currently logged-in user
    @GetMapping
    public String showProfile(Model model, Principal principal) {
        if (principal == null) {
            // If no user is logged in, redirect to login or show an error page
            return "redirect:/login";
        }

        // Fetch the logged-in user's username
        String username = principal.getName();

        // Find the user by their username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Fetch the user's links
        List<Link> userLinks = linkRepository.findAllByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("userLinks", userLinks);
        return "profile/view";
    }

    // Show form to edit profile
    @GetMapping("/edit-profile")
    public String showEditProfileForm(Model model, Principal principal) {
        if (principal == null) {
            // If no user is logged in, redirect to login or show an error page
            return "redirect:/login";
        }

        // Fetch the logged-in user's username
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        model.addAttribute("user", user);
        return "profile/edit-profile";
    }

    // Handle profile update
    @PostMapping("/edit-profile")
    public String updateProfile(@ModelAttribute User userDetails, Principal principal) {
        if (principal == null) {
            // If no user is logged in, redirect to login or show an error page
            return "redirect:/login";
        }

        // Fetch the logged-in user's username
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setFullName(userDetails.getFullName());
        user.setEmail(userDetails.getEmail());
        // Save updated user info
        userRepository.save(user);

        return "redirect:/profile";
    }

    // Show change password form
    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        // Add any necessary data for the form, if needed
        return "profile/change-password";
    }

    // Handle password change
    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Principal principal,
            Model model) {

        if (principal == null) {
            return "redirect:/login";
        }

        // Fetch the logged-in user's username
        String username = principal.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if the current password matches
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            model.addAttribute("error", "Current password is incorrect.");
            return "profile/change-password";
        }

        // Check if new password and confirm password match
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New password and confirm password do not match.");
            return "profile/change-password";
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Redirect back to the profile or show a success message
        model.addAttribute("success", "Password changed successfully.");
        return "redirect:/profile";  // You can redirect to profile or any other page
    }
}
