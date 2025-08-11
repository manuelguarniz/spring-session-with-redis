package com.example.infrastructure.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Test configuration that disables Spring Security for testing purposes
 * This configuration allows all requests to pass through without authentication
 */
@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // Deshabilitar CSRF para tests
        .csrf(csrf -> csrf.disable())

        // Permitir todos los requests sin autenticación
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll());

    return http.build();
  }
}