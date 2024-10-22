package com.linkease.repository;

import com.linkease.domain.ConfigType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigTypeRepository extends JpaRepository<ConfigType, Long> {
    // Check if a ConfigType with the given type already exists
    boolean existsByType(String type);
}
