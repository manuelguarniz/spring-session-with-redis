package com.example.infrastructure.adapter.out.persistence;

import com.example.application.port.out.ExchangeRateRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for exchange rate data
 * This class provides the actual implementation of exchange rate operations
 */
@Repository
public class ExchangeRateRepositoryImpl implements ExchangeRateRepository {

  // Current exchange rate: 1 USD = 3.8 PEN
  private static final double CURRENT_EXCHANGE_RATE = 3.8;

  @Override
  public double getCurrentExchangeRate() {
    return CURRENT_EXCHANGE_RATE;
  }
}