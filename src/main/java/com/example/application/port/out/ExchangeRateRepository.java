package com.example.application.port.out;

/**
 * Output port for exchange rate repository
 * This interface defines the contract for retrieving exchange rate data
 */
public interface ExchangeRateRepository {

  /**
   * Retrieves the current exchange rate from PEN to USD
   * 
   * @return current exchange rate (PEN per USD)
   */
  double getCurrentExchangeRate();
}