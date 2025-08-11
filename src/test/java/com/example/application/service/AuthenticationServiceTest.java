package com.example.application.service;

import com.example.application.dto.LoginRequest;
import com.example.application.dto.LoginResponse;
import com.example.application.port.out.UserRepository;
import com.example.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private SessionService sessionService;

  private AuthenticationService authenticationService;

  @BeforeEach
  void setUp() {
    authenticationService = new AuthenticationService(userRepository, passwordEncoder, sessionService);
  }

  @Test
  void authenticateAndCreateSession_ValidCredentials_ShouldReturnSuccessAndCreateSession() {
    // Given
    LoginRequest loginRequest = new LoginRequest("admin", "admin123");
    User user = createTestUser();

    when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("admin123", "encodedPassword")).thenReturn(true);

    // When
    LoginResponse response = authenticationService.authenticateAndCreateSession(loginRequest);

    // Then
    assertTrue(response.isSuccess());
    assertEquals("Authentication successful", response.getMessage());
    assertNotNull(response.getUser());
    assertEquals("admin", response.getUser().getUsername());
    assertEquals(Set.of("ADMIN", "USER"), response.getUser().getRoles());

    // Verificar que se creó la sesión
    verify(sessionService).createSession(any(User.class));

    verify(userRepository).findByUsername("admin");
    verify(passwordEncoder).matches("admin123", "encodedPassword");
  }

  @Test
  void authenticateAndCreateSession_InvalidCredentials_ShouldReturnFailureAndNotCreateSession() {
    // Given
    LoginRequest loginRequest = new LoginRequest("nonexistent", "password");

    when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

    // When
    LoginResponse response = authenticationService.authenticateAndCreateSession(loginRequest);

    // Then
    assertFalse(response.isSuccess());
    assertEquals("Invalid username or password", response.getMessage());
    assertNull(response.getUser());

    // Verificar que NO se creó la sesión
    verify(sessionService, never()).createSession(any(User.class));

    verify(userRepository).findByUsername("nonexistent");
    verify(passwordEncoder, never()).matches(anyString(), anyString());
  }

  @Test
  void authenticate_ValidCredentials_ShouldReturnSuccess() {
    // Given
    LoginRequest loginRequest = new LoginRequest("admin", "admin123");
    User user = createTestUser();

    when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("admin123", "encodedPassword")).thenReturn(true);

    // When
    LoginResponse response = authenticationService.authenticate(loginRequest);

    // Then
    assertTrue(response.isSuccess());
    assertEquals("Authentication successful", response.getMessage());
    assertNotNull(response.getUser());
    assertEquals("admin", response.getUser().getUsername());
    assertEquals(Set.of("ADMIN", "USER"), response.getUser().getRoles());

    verify(userRepository).findByUsername("admin");
    verify(passwordEncoder).matches("admin123", "encodedPassword");
  }

  @Test
  void authenticate_InvalidUsername_ShouldReturnFailure() {
    // Given
    LoginRequest loginRequest = new LoginRequest("nonexistent", "password");

    when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

    // When
    LoginResponse response = authenticationService.authenticate(loginRequest);

    // Then
    assertFalse(response.isSuccess());
    assertEquals("Invalid username or password", response.getMessage());
    assertNull(response.getUser());

    verify(userRepository).findByUsername("nonexistent");
    verify(passwordEncoder, never()).matches(anyString(), anyString());
  }

  @Test
  void authenticate_InvalidPassword_ShouldReturnFailure() {
    // Given
    LoginRequest loginRequest = new LoginRequest("admin", "wrongpassword");
    User user = createTestUser();

    when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrongpassword", "encodedPassword")).thenReturn(false);

    // When
    LoginResponse response = authenticationService.authenticate(loginRequest);

    // Then
    assertFalse(response.isSuccess());
    assertEquals("Invalid username or password", response.getMessage());
    assertNull(response.getUser());

    verify(userRepository).findByUsername("admin");
    verify(passwordEncoder).matches("wrongpassword", "encodedPassword");
  }

  @Test
  void authenticate_DisabledUser_ShouldReturnFailure() {
    // Given
    LoginRequest loginRequest = new LoginRequest("disabled", "password");
    User user = createTestUser();
    user.setEnabled(false);

    when(userRepository.findByUsername("disabled")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

    // When
    LoginResponse response = authenticationService.authenticate(loginRequest);

    // Then
    assertFalse(response.isSuccess());
    assertEquals("User account is disabled", response.getMessage());
    assertNull(response.getUser());

    verify(userRepository).findByUsername("disabled");
    verify(passwordEncoder).matches("password", "encodedPassword");
  }

  @Test
  void authenticate_EmptyUsername_ShouldReturnFailure() {
    // Given
    LoginRequest loginRequest = new LoginRequest("", "password");

    // When
    LoginResponse response = authenticationService.authenticate(loginRequest);

    // Then
    assertFalse(response.isSuccess());
    assertEquals("Username is required", response.getMessage());
    assertNull(response.getUser());

    verify(userRepository, never()).findByUsername(anyString());
    verify(passwordEncoder, never()).matches(anyString(), anyString());
  }

  @Test
  void authenticate_EmptyPassword_ShouldReturnFailure() {
    // Given
    LoginRequest loginRequest = new LoginRequest("admin", "");

    // When
    LoginResponse response = authenticationService.authenticate(loginRequest);

    // Then
    assertFalse(response.isSuccess());
    assertEquals("Password is required", response.getMessage());
    assertNull(response.getUser());

    verify(userRepository, never()).findByUsername(anyString());
    verify(passwordEncoder, never()).matches(anyString(), anyString());
  }

  @Test
  void authenticate_NullUsername_ShouldReturnFailure() {
    // Given
    LoginRequest loginRequest = new LoginRequest(null, "password");

    // When
    LoginResponse response = authenticationService.authenticate(loginRequest);

    // Then
    assertFalse(response.isSuccess());
    assertEquals("Username is required", response.getMessage());
    assertNull(response.getUser());

    verify(userRepository, never()).findByUsername(anyString());
    verify(passwordEncoder, never()).matches(anyString(), anyString());
  }

  @Test
  void authenticate_NullPassword_ShouldReturnFailure() {
    // Given
    LoginRequest loginRequest = new LoginRequest("admin", null);

    // When
    LoginResponse response = authenticationService.authenticate(loginRequest);

    // Then
    assertFalse(response.isSuccess());
    assertEquals("Password is required", response.getMessage());
    assertNull(response.getUser());

    verify(userRepository, never()).findByUsername(anyString());
    verify(passwordEncoder, never()).matches(anyString(), anyString());
  }

  private User createTestUser() {
    User user = new User();
    user.setId(1L);
    user.setUsername("admin");
    user.setPassword("encodedPassword");
    user.setEmail("admin@example.com");
    user.addRole("ADMIN");
    user.addRole("USER");
    user.setEnabled(true);
    return user;
  }
}