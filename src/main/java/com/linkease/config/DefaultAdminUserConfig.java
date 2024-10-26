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
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class DefaultAdminUserConfig {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Transactional
    public CommandLineRunner createDefaultAdmin() {
        return args -> {
            String defaultAdminUsername = "admin";
            String defaultAdminEmail = "admin@example.com";

            // Fetch the admin role from the database
            Role adminRole = roleRepository.findByName("ROLES_ADMIN")
                    .orElseGet(() -> {
                        Role newRole = new Role();
                        newRole.setName("ROLES_ADMIN");
                        return roleRepository.save(newRole); // Save and return new admin role
                    });

            // Check if the admin user exists
            if (userRepository.findByUsername(defaultAdminUsername).isEmpty()) {
                // Create and persist a new admin user
                User admin = new User();
                admin.setUsername(defaultAdminUsername);
                admin.setEmail(defaultAdminEmail);
                admin.setPassword(passwordEncoder.encode("admin123"));

                // Save the admin user first
                admin = userRepository.save(admin);

                // After user creation, attach the existing admin role to the user
                admin.setRoles(Collections.singleton(adminRole));

                // Update the user with the role information
                userRepository.save(admin);

                System.out.println("Default admin user created with username: admin and password: admin123");
            }
        };
    }
}
