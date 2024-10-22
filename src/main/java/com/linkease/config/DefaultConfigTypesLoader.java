package com.linkease.config;

import com.linkease.domain.ConfigType;
import com.linkease.repository.ConfigTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DefaultConfigTypesLoader {

    private final ConfigTypeRepository configTypeRepository;

    @Bean
    @Transactional
    public CommandLineRunner createDefaultConfigTypes() {
        return args -> {
            // List of default config types
            List<String> defaultConfigTypes = List.of(
                    "Text",
                    "List",
                    "Boolean",
                    "Date",
                    "Color",
                    "Number",
                    "Double"
            );

            // For each default type, check if it exists, and if not, save it
            for (String type : defaultConfigTypes) {
                if (!configTypeRepository.existsByType(type)) {
                    ConfigType configType = new ConfigType();
                    configType.setType(type);
                    configTypeRepository.save(configType);
                    System.out.println("Added default ConfigType: " + type);
                }
            }
        };
    }
}
