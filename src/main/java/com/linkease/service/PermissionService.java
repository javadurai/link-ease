package com.linkease.service;

import com.linkease.domain.Permission;
import com.linkease.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission findById(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found"));
    }

    public void save(Permission permission) {
        permissionRepository.save(permission);
    }

    public void update(Long id, Permission updatedPermission) {
        Permission existingPermission = findById(id);
        existingPermission.setName(updatedPermission.getName());
        existingPermission.setType(updatedPermission.getType());
        permissionRepository.save(existingPermission);
    }

    public void delete(Long id) {
        permissionRepository.deleteById(id);
    }
}
