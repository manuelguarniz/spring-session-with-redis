package com.example.application.port.in;

import com.example.application.dto.LoginRequest;
import com.example.application.dto.LoginResponse;

/**
 * Port for authentication operations
 * This interface defines the contract for authentication use cases
 */
public interface AuthenticationUseCase {

  /**
   * Authenticates a user and creates a session
   * 
   * @param loginRequest the login credentials
   * @return authentication result with user information
   */
  LoginResponse authenticateAndCreateSession(LoginRequest loginRequest);

  /**
   * Authenticates a user without creating a session
   * 
   * @param loginRequest the login credentials
   * @return authentication result
   */
  LoginResponse authenticate(LoginRequest loginRequest);
}