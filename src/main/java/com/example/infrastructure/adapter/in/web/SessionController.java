package com.example.infrastructure.adapter.in.web;

import com.example.application.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for session management endpoints
 * This controller handles session-related HTTP requests
 */
@RestController
@RequestMapping("/api/session")
public class SessionController {

  private final SessionService sessionService;

  public SessionController(SessionService sessionService) {
    this.sessionService = sessionService;
  }

  /**
   * Get current session information
   * 
   * @return session details or 401 if not authenticated
   */
  @GetMapping
  public ResponseEntity<Map<String, Object>> getSessionInfo() {
    if (!sessionService.isSessionValid()) {
      return ResponseEntity.status(401).build();
    }

    Map<String, Object> sessionInfo = sessionService.getSessionInfo();
    return ResponseEntity.ok(sessionInfo);
  }

  /**
   * Validate if current session is valid
   * 
   * @return 200 OK if session is valid, 401 if not
   */
  @GetMapping("/validate")
  public ResponseEntity<String> validateSession() {
    if (sessionService.isSessionValid()) {
      return ResponseEntity.ok("Session is valid");
    } else {
      return ResponseEntity.status(401).body("Session is invalid or expired");
    }
  }

  /**
   * Logout and invalidate current session
   * 
   * @return success message
   */
  @PostMapping("/logout")
  public ResponseEntity<Map<String, String>> logout() {
    sessionService.invalidateSession();

    Map<String, String> response = Map.of(
        "message", "Logout successful",
        "status", "success");

    return ResponseEntity.ok(response);
  }

  /**
   * Get current user information from session
   * 
   * @return user details or 401 if not authenticated
   */
  @GetMapping("/user")
  public ResponseEntity<Map<String, Object>> getCurrentUser() {
    if (!sessionService.isSessionValid()) {
      return ResponseEntity.status(401).build();
    }

    var currentUser = sessionService.getCurrentUser();
    Map<String, Object> userInfo = Map.of(
        "id", currentUser.getId(),
        "username", currentUser.getUsername(),
        "email", currentUser.getEmail(),
        "roles", currentUser.getRoles(),
        "enabled", currentUser.isEnabled());

    return ResponseEntity.ok(userInfo);
  }
}