package com.example.infrastructure.adapter.out.persistence;

import com.example.application.port.out.HealthCheckRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for health check data
 * This class provides the actual implementation of data access operations
 */
@Repository
public class HealthCheckRepositoryImpl implements HealthCheckRepository {

  @Override
  public String getHealthStatus() {
    return "OK";
  }
}