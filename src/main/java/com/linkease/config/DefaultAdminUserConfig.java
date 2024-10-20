package com.linkease.config;

import com.linkease.domain.Role;
import com.linkease.domain.User;
import com.linkease.repository.RoleRepository;
import com.linkease.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class DefaultAdminUserConfig {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner createDefaultAdmin() {
        return args -> {
            String defaultAdminUsername = "admin";
            String defaultAdminEmail = "admin@example.com";

            // Check if the admin role exists in the database, fetch or create it
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName("ROLE_ADMIN");
                        return roleRepository.save(newRole); // Save new admin role
                    });

            // Check if the admin user exists
            if (userRepository.findByUsername(defaultAdminUsername).isEmpty()) {
                // Create and persist a new admin user
                User admin = new User();
                admin.setUsername(defaultAdminUsername);
                admin.setEmail(defaultAdminEmail);
                admin.setPassword(passwordEncoder.encode("admin123"));

                // Fetch and attach the existing admin role to the user
                admin.setRoles(Collections.singleton(adminRole));

                // Save the admin user, Hibernate will manage the role relationship
                userRepository.save(admin);
                System.out.println("Default admin user created with username: admin and password: admin123");
            }
        };
    }
}
