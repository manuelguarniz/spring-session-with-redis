package com.example.infrastructure.adapter.in.web;

import com.example.application.port.in.HealthCheckUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Web controller for health check endpoints
 * This class handles HTTP requests for health check functionality
 */
@RestController
@RequestMapping("/api")
public class HealthCheckController {

  private final HealthCheckUseCase healthCheckUseCase;

  public HealthCheckController(HealthCheckUseCase healthCheckUseCase) {
    this.healthCheckUseCase = healthCheckUseCase;
  }

  /**
   * Health check endpoint
   * 
   * @return OK status message
   */
  @GetMapping("/health")
  public String health() {
    return healthCheckUseCase.checkHealth();
  }
}