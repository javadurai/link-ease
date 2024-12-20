package com.linkease.config;

import com.linkease.security.CustomAuthenticationEntryPoint;
import com.linkease.security.CustomAuthenticationSuccessHandler;
import com.linkease.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final ConfigLoader configLoader;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authenticationProvider())
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection for simplicity (adjust based on your app)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/favicon.ico").permitAll() // Public access for registration and login
                        .anyRequest().authenticated() // All other requests need authentication
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .permitAll() // Allow everyone to access the login page
                        .successHandler(new CustomAuthenticationSuccessHandler())
                        .failureUrl("/login?error=true") // Redirect to login with error message on failure
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Define the logout URL
                        .logoutSuccessUrl("/login?logout=true") // Redirect to login after logout
                        .permitAll() // Allow everyone to access the logout
                )
//                .sessionManagement(session -> session
//                        .maximumSessions(1) // Allow only one active session per user
//                        .expiredUrl("/login?expired=true") // Redirect to login if session expires
//                )
//                .sessionManagement(session -> session
//                        .invalidSessionUrl("/login?invalid=true") // Redirect if session is invalid
//                        .sessionFixation().migrateSession() // Control session fixation protection
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // Create session only if needed
//                        //.sessionTimeout(1800) // Set session timeout in seconds (e.g., 1800 seconds = 30 minutes)
//                )
        ;

        return http.build();
    }
}
