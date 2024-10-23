package com.linkease.controller;

import com.linkease.domain.Permission;
import com.linkease.domain.Role;
import com.linkease.dto.PermissionDTO;
import com.linkease.dto.PermissionType;
import com.linkease.repository.RoleRepository;
import com.linkease.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;
    private final RoleRepository roleRepository;

    @GetMapping
    public String listPermissions(Model model) {
        List<Permission> permissions = permissionService.findAll();
        model.addAttribute("permissions", permissions);
        return "permissions/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("permission", new Permission());
        model.addAttribute("permissionTypes", PermissionType.values());  // Pass enum values
        return "permissions/create";
    }

    @PostMapping("/create")
    public String createPermission(@ModelAttribute Permission permission, @RequestParam("name") String suffix) {
        String prefix = "";
        if (permission.getType() == PermissionType.PAGE) {
            prefix = "page.";
        } else if (permission.getType() == PermissionType.JOB) {
            prefix = "job.";
        } else if (permission.getType() == PermissionType.BUTTON) {
            prefix = "button.";
        }

        // Concatenate prefix and suffix
        permission.setName(prefix + suffix);

        permissionService.save(permission);
        return "redirect:/permissions";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Permission permission = permissionService.findById(id);
        model.addAttribute("permission", permission);
        model.addAttribute("permissionTypes", PermissionType.values());  // Pass enum values
        return "permissions/edit";
    }

    @PostMapping("/edit/{id}")
    public String updatePermission(@PathVariable Long id, @ModelAttribute Permission permission, @RequestParam("name") String suffix) {
        String prefix = "";
        if (permission.getType() == PermissionType.PAGE) {
            prefix = "page.";
        } else if (permission.getType() == PermissionType.JOB) {
            prefix = "job.";
        } else if (permission.getType() == PermissionType.BUTTON) {
            prefix = "button.";
        }

        // Concatenate prefix and suffix
        permission.setName(prefix + suffix);

        permissionService.update(id, permission);
        return "redirect:/permissions";
    }

    @GetMapping("/delete/{id}")
    public String deletePermission(@PathVariable Long id) {
        permissionService.delete(id);
        return "redirect:/permissions";
    }

    @GetMapping("/role/{roleId}")
    @ResponseBody
    public List<PermissionDTO> getPermissionsByRole(@PathVariable Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new IllegalArgumentException("Invalid role ID"));
        List<Permission> allPermissions = permissionService.findAll();

        // Map permissions with assigned status
        return allPermissions.stream()
                .map(permission -> {
                    boolean isAssigned = role.getPermissions().contains(permission);
                    return new PermissionDTO(permission.getId(), permission.getName(), isAssigned);
                })
                .collect(Collectors.toList());
    }

}
