package com.example.application.service;

import com.example.application.port.in.AuthenticationUseCase;
import com.example.application.port.out.UserRepository;
import com.example.application.dto.LoginRequest;
import com.example.application.dto.LoginResponse;
import com.example.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Application service that implements the authentication use case
 * This class orchestrates the business logic for user authentication
 */
@Service
public class AuthenticationService implements AuthenticationUseCase {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public LoginResponse authenticate(LoginRequest loginRequest) {
    // Validar que los campos no estén vacíos
    if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
      return new LoginResponse(false, "Username is required");
    }

    if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
      return new LoginResponse(false, "Password is required");
    }

    // Buscar usuario por username
    Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername().trim());

    if (userOpt.isEmpty()) {
      return new LoginResponse(false, "Invalid username or password");
    }

    User user = userOpt.get();

    // Verificar que el usuario esté habilitado
    if (!user.isEnabled()) {
      return new LoginResponse(false, "User account is disabled");
    }

    // Verificar contraseña
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      return new LoginResponse(false, "Invalid username or password");
    }

    // Autenticación exitosa
    LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getRoles());

    // Por ahora, el token es null (se implementará después)
    return new LoginResponse(true, "Authentication successful", null, userInfo);
  }
}