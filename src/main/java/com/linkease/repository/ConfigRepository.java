package com.linkease.repository;

import com.linkease.domain.Config;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfigRepository extends JpaRepository<Config, Long> {
    // Find all enabled configs
    List<Config> findAllByIsEnabled(boolean isEnabled);
}
