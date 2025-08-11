package com.example.domain.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Domain model for User entity
 */
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;
  private String username;
  private String password;
  private String email;
  private Set<String> roles;
  private boolean enabled;

  public User() {
    this.roles = new HashSet<>();
    this.enabled = true;
  }

  public User(String username, String password, String email) {
    this();
    this.username = username;
    this.password = password;
    this.email = email;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void addRole(String role) {
    this.roles.add(role);
  }

  public void removeRole(String role) {
    this.roles.remove(role);
  }
}