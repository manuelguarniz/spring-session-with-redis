package com.example.infrastructure.adapter.out.persistence;

import com.example.application.port.out.UserRepository;
import com.example.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory implementation of user repository
 * This is a temporary implementation for development purposes
 */
@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {

  private final Map<String, User> usersByUsername = new HashMap<>();
  private final Map<String, User> usersByEmail = new HashMap<>();
  private final PasswordEncoder passwordEncoder;

  public InMemoryUserRepositoryImpl(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @PostConstruct
  public void initializeUsers() {
    // Crear usuario de prueba
    User adminUser = new User();
    adminUser.setId(1L);
    adminUser.setUsername("admin");
    adminUser.setPassword(passwordEncoder.encode("admin123"));
    adminUser.setEmail("admin@example.com");
    adminUser.addRole("ADMIN");
    adminUser.addRole("USER");
    adminUser.setEnabled(true);

    User regularUser = new User();
    regularUser.setId(2L);
    regularUser.setUsername("user");
    regularUser.setPassword(passwordEncoder.encode("user123"));
    regularUser.setEmail("user@example.com");
    regularUser.addRole("USER");
    regularUser.setEnabled(true);

    // Guardar usuarios
    save(adminUser);
    save(regularUser);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return Optional.ofNullable(usersByUsername.get(username));
  }

  @Override
  public Optional<User> findByEmail(String email) {
    return Optional.ofNullable(usersByEmail.get(email));
  }

  @Override
  public User save(User user) {
    if (user.getId() == null) {
      // Generar ID si no existe
      user.setId((long) (usersByUsername.size() + 1));
    }

    // Guardar en ambos maps
    usersByUsername.put(user.getUsername(), user);
    usersByEmail.put(user.getEmail(), user);

    return user;
  }
}