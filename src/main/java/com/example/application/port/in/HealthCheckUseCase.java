package com.example.application.port.in;

/**
 * Input port for health check use case
 * This interface defines the contract for the health check functionality
 */
public interface HealthCheckUseCase {

  /**
   * Performs a health check
   * 
   * @return health status message
   */
  String checkHealth();
}