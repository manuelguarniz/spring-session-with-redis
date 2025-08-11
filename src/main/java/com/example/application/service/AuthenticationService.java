package com.example.application.service;

import com.example.application.port.in.AuthenticationUseCase;
import com.example.application.port.out.UserRepository;
import com.example.application.dto.LoginRequest;
import com.example.application.dto.LoginResponse;
import com.example.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Service for user authentication
 * This service implements the authentication use case
 */
@Service
public class AuthenticationService implements AuthenticationUseCase {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final SessionService sessionService;

  public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder,
      SessionService sessionService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.sessionService = sessionService;
  }

  @Override
  public LoginResponse authenticateAndCreateSession(LoginRequest loginRequest) {
    // Primero autenticar al usuario
    LoginResponse authResponse = authenticate(loginRequest);

    if (authResponse.isSuccess()) {
      // Si la autenticación es exitosa, crear la sesión
      User user = getUserFromResponse(authResponse);
      sessionService.createSession(user);
    }

    return authResponse;
  }

  @Override
  public LoginResponse authenticate(LoginRequest loginRequest) {
    // Validar que username y password no estén vacíos
    if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
      return new LoginResponse(false, "Username is required");
    }

    if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
      return new LoginResponse(false, "Password is required");
    }

    // Buscar usuario por username
    var userOpt = userRepository.findByUsername(loginRequest.getUsername().trim());
    if (userOpt.isEmpty()) {
      return new LoginResponse(false, "Invalid username or password");
    }

    User user = userOpt.get();

    // Verificar password
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      return new LoginResponse(false, "Invalid username or password");
    }

    // Verificar que el usuario esté habilitado
    if (!user.isEnabled()) {
      return new LoginResponse(false, "User account is disabled");
    }

    // Crear respuesta exitosa
    LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getRoles());

    return new LoginResponse(true, "Authentication successful", null, userInfo);
  }

  /**
   * Helper method to extract User from LoginResponse
   * 
   * @param response the login response
   * @return User object
   */
  private User getUserFromResponse(LoginResponse response) {
    User user = new User();
    user.setId(response.getUser().getId());
    user.setUsername(response.getUser().getUsername());
    user.setEmail(response.getUser().getEmail());
    user.setRoles(response.getUser().getRoles());
    user.setEnabled(true);
    return user;
  }
}