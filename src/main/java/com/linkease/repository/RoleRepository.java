package com.linkease.repository;

import com.linkease.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    Page<Role> findByNameContaining(String name, Pageable pageable);

    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions")
    Page<Role> findAllWithPermissions(Pageable pageable);
}
