package com.example.application.port.out;

import com.example.domain.model.User;
import java.util.Optional;

/**
 * Output port for user repository
 * This interface defines the contract for user data access operations
 */
public interface UserRepository {

  /**
   * Finds a user by username
   * 
   * @param username the username to search for
   * @return Optional containing the user if found
   */
  Optional<User> findByUsername(String username);

  /**
   * Finds a user by email
   * 
   * @param email the email to search for
   * @return Optional containing the user if found
   */
  Optional<User> findByEmail(String email);

  /**
   * Saves a user
   * 
   * @param user the user to save
   * @return the saved user
   */
  User save(User user);
}