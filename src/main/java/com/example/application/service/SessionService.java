package com.example.application.service;

import com.example.domain.model.User;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for managing user sessions
 * This service handles session-related operations
 */
@Service
public class SessionService {

  private static final String USER_SESSION_KEY = "currentUser";
  private static final String SESSION_CREATED_TIME_KEY = "sessionCreatedTime";

  /**
   * Creates a new session for the authenticated user
   * 
   * @param user the authenticated user
   * @return session information
   */
  public Map<String, Object> createSession(User user) {
    try {
      HttpSession session = getCurrentSession();

      // Guardar información del usuario en la sesión
      session.setAttribute(USER_SESSION_KEY, user);
      session.setAttribute(SESSION_CREATED_TIME_KEY, System.currentTimeMillis());

      // Configurar tiempo de inactividad (30 minutos)
      session.setMaxInactiveInterval(1800);

      Map<String, Object> sessionInfo = new HashMap<>();
      sessionInfo.put("sessionId", session.getId());
      sessionInfo.put("userId", user.getId());
      sessionInfo.put("username", user.getUsername());
      sessionInfo.put("createdTime", session.getAttribute(SESSION_CREATED_TIME_KEY));
      sessionInfo.put("maxInactiveInterval", session.getMaxInactiveInterval());

      return sessionInfo;
    } catch (Exception e) {
      // Fallback: crear información de sesión básica
      Map<String, Object> sessionInfo = new HashMap<>();
      sessionInfo.put("sessionId", "fallback-session");
      sessionInfo.put("userId", user.getId());
      sessionInfo.put("username", user.getUsername());
      sessionInfo.put("createdTime", System.currentTimeMillis());
      sessionInfo.put("maxInactiveInterval", 1800);
      return sessionInfo;
    }
  }

  /**
   * Gets the current user from the session
   * 
   * @return the current user or null if not authenticated
   */
  public User getCurrentUser() {
    try {
      HttpSession session = getCurrentSession();
      return (User) session.getAttribute(USER_SESSION_KEY);
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Gets current session information
   * 
   * @return session information
   */
  public Map<String, Object> getSessionInfo() {
    try {
      HttpSession session = getCurrentSession();
      User currentUser = getCurrentUser();

      if (currentUser == null) {
        return null;
      }

      Map<String, Object> sessionInfo = new HashMap<>();
      sessionInfo.put("sessionId", session.getId());
      sessionInfo.put("userId", currentUser.getId());
      sessionInfo.put("username", currentUser.getUsername());
      sessionInfo.put("email", currentUser.getEmail());
      sessionInfo.put("roles", currentUser.getRoles());
      sessionInfo.put("createdTime", session.getAttribute(SESSION_CREATED_TIME_KEY));
      sessionInfo.put("lastAccessedTime", session.getLastAccessedTime());
      sessionInfo.put("maxInactiveInterval", session.getMaxInactiveInterval());
      sessionInfo.put("isNew", session.isNew());

      return sessionInfo;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Invalidates the current session
   */
  public void invalidateSession() {
    try {
      HttpSession session = getCurrentSession();
      session.invalidate();
    } catch (Exception e) {
      // Ignore errors during session invalidation
    }
  }

  /**
   * Checks if the current session is valid
   * 
   * @return true if session is valid, false otherwise
   */
  public boolean isSessionValid() {
    try {
      HttpSession session = getCurrentSession();
      return session != null && getCurrentUser() != null;
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Gets the current HTTP session
   * 
   * @return the current session
   */
  private HttpSession getCurrentSession() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      return attributes.getRequest().getSession(true);
    }
    throw new IllegalStateException("No request context available");
  }
}