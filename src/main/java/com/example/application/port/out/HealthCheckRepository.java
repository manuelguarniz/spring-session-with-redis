package com.example.application.port.out;

/**
 * Output port for health check repository
 * This interface defines the contract for data access operations
 */
public interface HealthCheckRepository {

  /**
   * Retrieves health status from the repository
   * 
   * @return health status message
   */
  String getHealthStatus();
}