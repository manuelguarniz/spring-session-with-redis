package com.example.application.port.in;

import com.example.application.dto.LoginRequest;
import com.example.application.dto.LoginResponse;

/**
 * Input port for authentication use case
 * This interface defines the contract for user authentication
 */
public interface AuthenticationUseCase {

  /**
   * Authenticates a user with username and password
   * 
   * @param loginRequest login credentials
   * @return authentication result
   */
  LoginResponse authenticate(LoginRequest loginRequest);
}