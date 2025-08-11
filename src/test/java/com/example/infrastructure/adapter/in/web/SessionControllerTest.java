package com.example.infrastructure.adapter.in.web;

import com.example.application.service.SessionService;
import com.example.domain.model.User;
import com.example.infrastructure.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SessionController.class)
@Import(TestSecurityConfig.class)
class SessionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SessionService sessionService;

  @Test
  void getSessionInfo_ValidSession_ShouldReturnSessionInfo() throws Exception {
    // Given
    User testUser = createTestUser();
    Map<String, Object> sessionInfo = createSessionInfo(testUser);

    when(sessionService.isSessionValid()).thenReturn(true);
    when(sessionService.getSessionInfo()).thenReturn(sessionInfo);

    // When & Then
    mockMvc.perform(get("/api/session"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sessionId").value("test-session-id"))
        .andExpect(jsonPath("$.userId").value(1))
        .andExpect(jsonPath("$.username").value("testuser"));
  }

  @Test
  void getSessionInfo_InvalidSession_ShouldReturnUnauthorized() throws Exception {
    // Given
    when(sessionService.isSessionValid()).thenReturn(false);

    // When & Then
    mockMvc.perform(get("/api/session"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  void validateSession_ValidSession_ShouldReturnOk() throws Exception {
    // Given
    when(sessionService.isSessionValid()).thenReturn(true);

    // When & Then
    mockMvc.perform(get("/api/session/validate"))
        .andExpect(status().isOk())
        .andExpect(content().string("Session is valid"));
  }

  @Test
  void validateSession_InvalidSession_ShouldReturnUnauthorized() throws Exception {
    // Given
    when(sessionService.isSessionValid()).thenReturn(false);

    // When & Then
    mockMvc.perform(get("/api/session/validate"))
        .andExpect(status().isUnauthorized())
        .andExpect(content().string("Session is invalid or expired"));
  }

  @Test
  void logout_ShouldReturnSuccessMessage() throws Exception {
    // Given
    doNothing().when(sessionService).invalidateSession();

    // When & Then
    mockMvc.perform(post("/api/session/logout"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Logout successful"))
        .andExpect(jsonPath("$.status").value("success"));

    verify(sessionService).invalidateSession();
  }

  @Test
  void getCurrentUser_ValidSession_ShouldReturnUserInfo() throws Exception {
    // Given
    User testUser = createTestUser();

    when(sessionService.isSessionValid()).thenReturn(true);
    when(sessionService.getCurrentUser()).thenReturn(testUser);

    // When & Then
    mockMvc.perform(get("/api/session/user"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.username").value("testuser"))
        .andExpect(jsonPath("$.email").value("test@example.com"))
        .andExpect(jsonPath("$.enabled").value(true))
        .andExpect(jsonPath("$.roles").isArray())
        .andExpect(jsonPath("$.roles[0]").value("USER"));
  }

  @Test
  void getCurrentUser_InvalidSession_ShouldReturnUnauthorized() throws Exception {
    // Given
    when(sessionService.isSessionValid()).thenReturn(false);

    // When & Then
    mockMvc.perform(get("/api/session/user"))
        .andExpect(status().isUnauthorized());
  }

  private User createTestUser() {
    User user = new User();
    user.setId(1L);
    user.setUsername("testuser");
    user.setEmail("test@example.com");
    user.addRole("USER");
    user.setEnabled(true);
    return user;
  }

  private Map<String, Object> createSessionInfo(User user) {
    Map<String, Object> sessionInfo = new HashMap<>();
    sessionInfo.put("sessionId", "test-session-id");
    sessionInfo.put("userId", user.getId());
    sessionInfo.put("username", user.getUsername());
    sessionInfo.put("createdTime", System.currentTimeMillis());
    sessionInfo.put("maxInactiveInterval", 1800);
    return sessionInfo;
  }
}