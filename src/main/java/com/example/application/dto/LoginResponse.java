package com.example.application.dto;

import java.util.Set;

/**
 * DTO for login response
 */
public class LoginResponse {

  private boolean success;
  private String message;
  private String token; // Placeholder for future JWT implementation
  private UserInfo user;

  public LoginResponse() {
  }

  public LoginResponse(boolean success, String message) {
    this.success = success;
    this.message = message;
  }

  public LoginResponse(boolean success, String message, String token, UserInfo user) {
    this.success = success;
    this.message = message;
    this.token = token;
    this.user = user;
  }

  // Getters and Setters
  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public UserInfo getUser() {
    return user;
  }

  public void setUser(UserInfo user) {
    this.user = user;
  }

  /**
   * Inner class for user information in response
   */
  public static class UserInfo {
    private Long id;
    private String username;
    private String email;
    private Set<String> roles;

    public UserInfo() {
    }

    public UserInfo(Long id, String username, String email, Set<String> roles) {
      this.id = id;
      this.username = username;
      this.email = email;
      this.roles = roles;
    }

    // Getters and Setters
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public Set<String> getRoles() {
      return roles;
    }

    public void setRoles(Set<String> roles) {
      this.roles = roles;
    }
  }
}