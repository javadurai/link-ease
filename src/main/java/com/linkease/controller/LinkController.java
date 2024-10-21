package com.linkease.controller;

import com.linkease.domain.Link;
import com.linkease.domain.User;
import com.linkease.repository.LinkRepository;
import com.linkease.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/links")
@RequiredArgsConstructor
public class LinkController {

    private final LinkRepository linkRepository;
    private final UserRepository userRepository;

    // List links with pagination
    @GetMapping
    public String getAllLinks(
            @RequestParam Optional<String> url,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Link> linkPage;

        if (url.isPresent()) {
            linkPage = linkRepository.findByUrlContaining(url.get(), pageable);
        } else {
            linkPage = linkRepository.findAll(pageable);
        }

        model.addAttribute("links", linkPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", linkPage.getTotalPages());
        model.addAttribute("totalItems", linkPage.getTotalElements());

        return "links/list";
    }

    // Show form to create a new link
    @GetMapping("/create")
    public String showCreateLinkForm(Model model) {
        model.addAttribute("link", new Link());
        return "links/create";
    }

    // Create a new link
    @PostMapping("/create")
    public String createLink(@ModelAttribute Link link) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the username of the logged-in user

        // Find the user from the database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        // Set the current date and user
        link.setCreatedAt(LocalDateTime.now());
        link.setUpdatedAt(LocalDateTime.now());
        link.setUser(user); // Set the logged-in user

        linkRepository.save(link);
        return "redirect:/links";
    }

    // Show form to edit an existing link
    @GetMapping("/edit/{id}")
    public String showEditLinkForm(@PathVariable Long id, Model model) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid link Id:" + id));
        model.addAttribute("link", link);
        return "links/edit";
    }

    // Update an existing link
    @PostMapping("/update/{id}")
    public String updateLink(@PathVariable Long id, @ModelAttribute Link linkDetails) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid link Id:" + id));

        link.setUrl(linkDetails.getUrl());
        link.setDescription(linkDetails.getDescription());
        link.setUpdatedAt(LocalDateTime.now());
        linkRepository.save(link);
        return "redirect:/links";
    }

    // Delete a link
    @GetMapping("/delete/{id}")
    public String deleteLink(@PathVariable Long id) {
        linkRepository.deleteById(id);
        return "redirect:/links";
    }
}
