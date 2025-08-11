package com.example.infrastructure.adapter.in.web;

import com.example.application.port.in.AuthenticationUseCase;
import com.example.application.dto.LoginRequest;
import com.example.application.dto.LoginResponse;
import com.example.application.service.SessionService;
import com.example.infrastructure.config.TestSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthenticationUseCase authenticationUseCase;

  @MockBean
  private SessionService sessionService;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void login_ValidCredentials_ShouldReturnSuccess() throws Exception {
    // Given
    LoginRequest loginRequest = new LoginRequest("admin", "admin123");
    LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(1L, "admin", "admin@example.com",
        Set.of("ADMIN", "USER"));
    LoginResponse loginResponse = new LoginResponse(true, "Authentication successful", null, userInfo);

    // Mock SessionService response
    Map<String, Object> sessionInfo = new HashMap<>();
    sessionInfo.put("sessionId", "test-session-id");
    sessionInfo.put("userId", 1L);
    sessionInfo.put("username", "admin");
    sessionInfo.put("createdTime", System.currentTimeMillis());
    sessionInfo.put("maxInactiveInterval", 1800);

    when(authenticationUseCase.authenticate(any(LoginRequest.class))).thenReturn(loginResponse);
    when(sessionService.createSession(any())).thenReturn(sessionInfo);

    // When & Then
    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Authentication successful"))
        .andExpect(jsonPath("$.user.username").value("admin"))
        .andExpect(jsonPath("$.user.roles").isArray())
        .andExpect(jsonPath("$.user.roles[0]").value("ADMIN"))
        .andExpect(jsonPath("$.user.roles[1]").value("USER"))
        .andExpect(jsonPath("$.session.sessionId").value("test-session-id"));
  }

  @Test
  void login_InvalidCredentials_ShouldReturnUnauthorized() throws Exception {
    // Given
    LoginRequest loginRequest = new LoginRequest("admin", "wrongpassword");
    LoginResponse loginResponse = new LoginResponse(false, "Invalid username or password");

    when(authenticationUseCase.authenticate(any(LoginRequest.class))).thenReturn(loginResponse);

    // When & Then
    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Invalid username or password"));
  }

  @Test
  void login_EmptyUsername_ShouldReturnUnauthorized() throws Exception {
    // Given
    LoginRequest loginRequest = new LoginRequest("", "password");
    LoginResponse loginResponse = new LoginResponse(false, "Username is required");

    when(authenticationUseCase.authenticate(any(LoginRequest.class))).thenReturn(loginResponse);

    // When & Then
    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Username is required"));
  }

  @Test
  void login_EmptyPassword_ShouldReturnUnauthorized() throws Exception {
    // Given
    LoginRequest loginRequest = new LoginRequest("admin", "");
    LoginResponse loginResponse = new LoginResponse(false, "Password is required");

    when(authenticationUseCase.authenticate(any(LoginRequest.class))).thenReturn(loginResponse);

    // When & Then
    mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Password is required"));
  }

  @Test
  void health_ShouldReturnOk() throws Exception {
    // When & Then
    mockMvc.perform(get("/api/auth/health"))
        .andExpect(status().isOk())
        .andExpect(content().string("Authentication service is running"));
  }
}