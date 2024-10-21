package com.linkease.repository;

import com.linkease.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    Page<Role> findByNameContaining(String name, Pageable pageable);

    //Set<Role> findAllById(List<Long> roleIds);
}
