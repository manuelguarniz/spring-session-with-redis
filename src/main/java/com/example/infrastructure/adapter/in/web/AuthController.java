package com.example.infrastructure.adapter.in.web;

import com.example.application.port.in.AuthenticationUseCase;
import com.example.application.dto.LoginRequest;
import com.example.application.dto.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Web controller for authentication endpoints
 * This class handles HTTP requests for authentication functionality
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  private final AuthenticationUseCase authenticationUseCase;

  public AuthController(AuthenticationUseCase authenticationUseCase) {
    this.authenticationUseCase = authenticationUseCase;
    logger.info("AuthController initialized with AuthenticationUseCase: {}",
        authenticationUseCase != null ? "OK" : "NULL");
  }

  /**
   * Login endpoint
   * 
   * @param loginRequest login credentials
   * @return authentication result with user information
   */
  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
    try {
      logger.info("Login attempt for user: {}", loginRequest.getUsername());

      // Usar el caso de uso que maneja autenticación y creación de sesión
      LoginResponse response = authenticationUseCase.authenticateAndCreateSession(loginRequest);
      logger.info("Authentication result: {}", response.isSuccess());

      if (response.isSuccess()) {
        // Configurar el contexto de seguridad de Spring Security
        configureSecurityContext(response.getUser());
        logger.info("Security context configured for user: {}", response.getUser().getUsername());

        // Crear respuesta simplificada con solo la información del usuario
        Map<String, Object> fullResponse = new HashMap<>();
        fullResponse.put("success", true);
        fullResponse.put("message", "Authentication successful");

        // Solo incluir username, email y roles del usuario
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", response.getUser().getUsername());
        userInfo.put("email", response.getUser().getEmail());
        userInfo.put("roles", response.getUser().getRoles());

        fullResponse.put("user", userInfo);

        return ResponseEntity.ok(fullResponse);
      } else {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("message", response.getMessage());
        return ResponseEntity.status(401).body(errorResponse);
      }
    } catch (Exception e) {
      logger.error("Error during login: ", e);
      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("success", false);
      errorResponse.put("message", "Internal server error: " + e.getMessage());
      return ResponseEntity.status(500).body(errorResponse);
    }
  }

  /**
   * Health check endpoint for authentication service
   * 
   * @return service status
   */
  @GetMapping("/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("Authentication service is running");
  }

  /**
   * Configure Spring Security context after successful authentication
   * 
   * @param userInfo the authenticated user info
   */
  private void configureSecurityContext(LoginResponse.UserInfo userInfo) {
    // Convertir roles a SimpleGrantedAuthority
    var authorities = userInfo.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
        .collect(Collectors.toList());

    // Crear token de autenticación
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        userInfo.getUsername(),
        null, // No password needed for session
        authorities);

    // Configurar el contexto de seguridad
    SecurityContextHolder.getContext().setAuthentication(authentication);
    logger.info("Security context set for user: {} with roles: {}", userInfo.getUsername(), userInfo.getRoles());
  }
}