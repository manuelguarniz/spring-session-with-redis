package com.example.application.service;

import com.example.application.port.in.HealthCheckUseCase;
import com.example.application.port.out.HealthCheckRepository;
import org.springframework.stereotype.Service;

/**
 * Application service that implements the health check use case
 * This class orchestrates the business logic for health checks
 */
@Service
public class HealthCheckService implements HealthCheckUseCase {

  private final HealthCheckRepository healthCheckRepository;

  public HealthCheckService(HealthCheckRepository healthCheckRepository) {
    this.healthCheckRepository = healthCheckRepository;
  }

  @Override
  public String checkHealth() {
    return healthCheckRepository.getHealthStatus();
  }
}