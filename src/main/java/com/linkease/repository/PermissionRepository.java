package com.linkease.repository;

import com.linkease.domain.Permission;
import com.linkease.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
    Page<Permission> findByNameContaining(String name, Pageable pageable);

    Page<Permission> findAll(Pageable pageable);
}
