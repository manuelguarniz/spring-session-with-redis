package com.example.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Spring Security configuration
 * This class configures security settings for the application
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final CorsConfigurationSource corsConfigurationSource;

  public SecurityConfig(CorsConfigurationSource corsConfigurationSource) {
    this.corsConfigurationSource = corsConfigurationSource;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        // Habilitar CORS
        .cors(cors -> cors.configurationSource(corsConfigurationSource))

        // Deshabilitar CSRF para APIs REST
        .csrf(csrf -> csrf.disable())

        // Configurar gestión de sesiones
        .sessionManagement(session -> session
            .sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(1) // Solo una sesión activa por usuario
            .expiredUrl("/api/auth/login"))

        // Configurar autorización de requests
        .authorizeHttpRequests(auth -> auth
            // Endpoints públicos
            .requestMatchers("/api/auth/**", "/api/health", "/actuator/**").permitAll()
            // Endpoints de conversión de moneda (requieren autenticación)
            .requestMatchers("/api/currency/**").authenticated()
            // Endpoint de sesión (requiere autenticación)
            .requestMatchers("/api/session/**").authenticated()
            // Cualquier otro request requiere autenticación
            .anyRequest().authenticated())

        // Configurar el repositorio de contexto de seguridad para usar sesiones
        .securityContext(securityContext -> securityContext.requireExplicitSave(false));

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }
}