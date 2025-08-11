package com.example.application.service;

import com.example.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

  private SessionService sessionService;
  private User testUser;
  private MockHttpServletRequest mockRequest;
  private MockHttpSession mockSession;

  @BeforeEach
  void setUp() {
    sessionService = new SessionService();

    // Crear usuario de prueba
    testUser = new User();
    testUser.setId(1L);
    testUser.setUsername("testuser");
    testUser.setEmail("test@example.com");
    testUser.addRole("USER");
    testUser.setEnabled(true);

    // Configurar mock de request y session
    mockRequest = new MockHttpServletRequest();
    mockSession = new MockHttpSession();
    mockRequest.setSession(mockSession);

    // Configurar RequestContextHolder
    ServletRequestAttributes attributes = new ServletRequestAttributes(mockRequest);
    RequestContextHolder.setRequestAttributes(attributes);
  }

  @Test
  void createSession_ShouldCreateValidSession() {
    // When
    var sessionInfo = sessionService.createSession(testUser);

    // Then
    assertNotNull(sessionInfo);
    assertEquals(testUser.getId(), sessionInfo.get("userId"));
    assertEquals(testUser.getUsername(), sessionInfo.get("username"));
    assertNotNull(sessionInfo.get("sessionId"));
    assertNotNull(sessionInfo.get("createdTime"));
    assertEquals(1800, sessionInfo.get("maxInactiveInterval"));

    // Verificar que la sesión se creó correctamente
    assertTrue(sessionService.isSessionValid());
  }

  @Test
  void getCurrentUser_ShouldReturnUserFromSession() {
    // Given
    sessionService.createSession(testUser);

    // When
    User currentUser = sessionService.getCurrentUser();

    // Then
    assertNotNull(currentUser);
    assertEquals(testUser.getId(), currentUser.getId());
    assertEquals(testUser.getUsername(), currentUser.getUsername());
    assertEquals(testUser.getEmail(), currentUser.getEmail());
  }

  @Test
  void getSessionInfo_ShouldReturnCompleteSessionInfo() {
    // Given
    sessionService.createSession(testUser);

    // When
    var sessionInfo = sessionService.getSessionInfo();

    // Then
    assertNotNull(sessionInfo);
    assertEquals(testUser.getId(), sessionInfo.get("userId"));
    assertEquals(testUser.getUsername(), sessionInfo.get("username"));
    assertEquals(testUser.getEmail(), sessionInfo.get("email"));
    assertEquals(testUser.getRoles(), sessionInfo.get("roles"));
    // Nota: enabled no se incluye en getSessionInfo por ahora
    assertNotNull(sessionInfo.get("sessionId"));
    assertNotNull(sessionInfo.get("createdTime"));
    assertNotNull(sessionInfo.get("lastAccessedTime"));
    assertEquals(1800, sessionInfo.get("maxInactiveInterval"));
    assertNotNull(sessionInfo.get("isNew"));
  }

  @Test
  void isSessionValid_ShouldReturnTrueForValidSession() {
    // Given
    sessionService.createSession(testUser);

    // When
    boolean isValid = sessionService.isSessionValid();

    // Then
    assertTrue(isValid);
  }

  @Test
  void isSessionValid_ShouldReturnFalseForInvalidSession() {
    // When
    boolean isValid = sessionService.isSessionValid();

    // Then
    assertFalse(isValid);
  }

  @Test
  void invalidateSession_ShouldInvalidateCurrentSession() {
    // Given
    sessionService.createSession(testUser);
    assertTrue(sessionService.isSessionValid());

    // When
    sessionService.invalidateSession();

    // Then
    assertFalse(sessionService.isSessionValid());
    assertNull(sessionService.getCurrentUser());
  }

  @Test
  void getCurrentUser_ShouldReturnNullForInvalidSession() {
    // When
    User currentUser = sessionService.getCurrentUser();

    // Then
    assertNull(currentUser);
  }

  @Test
  void getSessionInfo_ShouldReturnNullForInvalidSession() {
    // When
    var sessionInfo = sessionService.getSessionInfo();

    // Then
    assertNull(sessionInfo);
  }
}