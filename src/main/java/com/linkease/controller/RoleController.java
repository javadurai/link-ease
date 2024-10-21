package com.linkease.controller;

import com.linkease.domain.Role;
import com.linkease.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;

    // List roles with pagination
    @GetMapping
    public String getAllRoles(
            @RequestParam Optional<String> name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Role> rolePage;

        if (name.isPresent()) {
            rolePage = roleRepository.findByNameContaining(name.get(), pageable);
        } else {
            rolePage = roleRepository.findAll(pageable);
        }

        model.addAttribute("roles", rolePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", rolePage.getTotalPages());
        model.addAttribute("totalItems", rolePage.getTotalElements());

        return "roles/list";
    }

    // Show form to create a new role
    @GetMapping("/create")
    public String showCreateRoleForm(Model model) {
        model.addAttribute("role", new Role());
        return "roles/create";
    }

    // Create a new role
    @PostMapping("/create")
    public String createRole(@ModelAttribute Role role) {
        roleRepository.save(role);
        return "redirect:/roles";
    }

    // Show form to edit an existing role
    @GetMapping("/edit/{id}")
    public String showEditRoleForm(@PathVariable Long id, Model model) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));
        model.addAttribute("role", role);
        return "roles/edit";
    }

    // Update an existing role
    @PostMapping("/update/{id}")
    public String updateRole(@PathVariable Long id, @ModelAttribute Role roleDetails) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));

        role.setName(roleDetails.getName());
        roleRepository.save(role);
        return "redirect:/roles";
    }

    // Delete a role
    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable Long id) {
        roleRepository.deleteById(id);
        return "redirect:/roles";
    }
}
