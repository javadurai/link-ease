package com.linkease.config;

import com.linkease.domain.Config;
import com.linkease.repository.ConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConfigLoader {

    private final ConfigRepository configRepository;
    private Map<String, String> configs;

    @PostConstruct
    public void loadConfigs() {
        // Load all enabled configs from the database
        configs = configRepository.findAllByIsEnabled(true)
                .stream()
                .collect(Collectors.toMap(Config::getConfigName, Config::getConfigValue));
    }

    public String getConfigValue(String configName) {
        return configs != null ? configs.get(configName) : null;
    }
}
