package com.linkease.config;

import com.linkease.domain.Config;
import com.linkease.repository.ConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConfigLoader {

    private final ConfigRepository configRepository;
    private Map<String, String> configs;
    private final Lock lock = new ReentrantLock();  // For thread-safety

    // Load configs initially when the bean is initialized
    @PostConstruct
    public void loadConfigs() {
        refreshConfigs();  // Call the refresh method during initialization
    }

    // Refresh the config map from the database
    public void refreshConfigs() {
        lock.lock();
        try {
            configs = configRepository.findAllByIsEnabled(true)
                    .stream()
                    .collect(Collectors.toMap(Config::getConfigName, Config::getConfigValue));
        } finally {
            lock.unlock();
        }
    }

    // Retrieve a specific config value by name
    public String getConfigValue(String configName) {
        lock.lock();
        try {
            return configs != null ? configs.get(configName) : null;
        } finally {
            lock.unlock();
        }
    }

    // Expose a method to get the entire config map
    public Map<String, String> getAllConfigs() {
        lock.lock();
        try {
            return configs;
        } finally {
            lock.unlock();
        }
    }
}
