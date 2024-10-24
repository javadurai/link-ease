package com.linkease.controller;

import com.linkease.domain.Permission;
import com.linkease.domain.Role;
import com.linkease.domain.User;
import com.linkease.dto.PermissionDTO;
import com.linkease.dto.RoleDTO;
import com.linkease.exception.ResourceNotFoundException;
import com.linkease.repository.PermissionRepository;
import com.linkease.repository.RoleRepository;
import com.linkease.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

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
            rolePage = roleRepository.findAllWithPermissions(pageable);
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

    @PostMapping("/assign-permissions")
    public String assignPermissions(@RequestParam("roleId") Long roleId, @RequestParam("permissions") List<Long> permissionIds) {
        Optional<Role> roleOptional = roleRepository.findById(roleId);
        // Check if the role exists
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            // Fetch the selected permissions from the database
            List<Permission> selectedPermissions = permissionRepository.findAllById(permissionIds);
            // Make sure the collection is initialized (use getPermissions() to force initialization if using Lazy fetching)
            role.getPermissions().clear();  // Clear existing permissions
            // Assign the permissions to the role
            role.setPermissions(new HashSet<>(selectedPermissions));
            // Save the role with updated permissions
            roleRepository.save(role);
        } else {
            // Handle the case when the role is not found (e.g., throw an exception or add a redirect with an error message)
            return "redirect:/roles?error=RoleNotFound";
        }
        return "redirect:/roles";
    }

    @GetMapping("/user/{userId}")
    @ResponseBody
    public List<RoleDTO> getRolesByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(    "Invalid user ID"));
        List<Role> allRoles = roleRepository.findAll();

        // Map permissions with assigned status
        return allRoles.stream()
                .map(role -> {
                    boolean isAssigned = user.getRoles().contains(role);
                    return new RoleDTO(role.getId(), role.getName(), isAssigned);
                })
                .collect(Collectors.toList());
    }

}
