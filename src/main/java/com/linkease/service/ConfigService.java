package com.linkease.service;

import com.linkease.domain.Config;
import com.linkease.domain.ConfigType;
import com.linkease.repository.ConfigRepository;
import com.linkease.repository.ConfigTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfigService {

    private final ConfigRepository configRepository;
    private  final ConfigTypeRepository configTypeRepository;

    public List<Config> getAllConfigs() {
        return configRepository.findAll();
    }

    public Config getConfigById(Long id) {
        return configRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid config Id: " + id));
    }

    public Config createConfig(Config config) {
        validateConfigValue(config);
        config.setCreatedAt(LocalDateTime.now());
        config.setUpdatedAt(LocalDateTime.now());
        return configRepository.save(config);
    }

    public Config updateConfig(Long id, Config configDetails) {
        Config config = configRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid config Id: " + id));
        config.setConfigValue(configDetails.getConfigValue());
        config.setIsEnabled(configDetails.getIsEnabled());
        config.setUpdatedAt(LocalDateTime.now());
        validateConfigValue(config);
        return configRepository.save(config);
    }

    public void deleteConfig(Long id) {
        configRepository.deleteById(id);
    }

    private void validateConfigValue(Config config) {
        // Perform validation based on the config type (optional)
        ConfigType configType = config.getConfigType();
        String value = config.getConfigValue();
        Optional<ConfigType> configTypeOptional = configTypeRepository.findById(configType.getId());
        if(configTypeOptional.isPresent()){
            configType = configTypeOptional.get();
        }

        switch (configType.getType()) {
            case "Boolean":
                if (!value.equals("true") && !value.equals("false")) {
                    throw new IllegalArgumentException("Invalid Boolean value");
                }
                break;
            case "Number":
                try {
                    Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid Number value");
                }
                break;
            case "Double":
                try {
                    Double.parseDouble(value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid Double value");
                }
                break;
            case "Date":
                try {
                    LocalDate.parse(value);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid Date value");
                }
                break;
            // Add additional validations as needed
        }
    }
}
