package com.example.application.service;

import com.example.application.port.in.CurrencyConversionUseCase;
import com.example.application.port.out.ExchangeRateRepository;
import org.springframework.stereotype.Service;

/**
 * Application service that implements the currency conversion use case
 * This class orchestrates the business logic for currency conversions
 */
@Service
public class CurrencyConversionService implements CurrencyConversionUseCase {

  private final ExchangeRateRepository exchangeRateRepository;

  public CurrencyConversionService(ExchangeRateRepository exchangeRateRepository) {
    this.exchangeRateRepository = exchangeRateRepository;
  }

  @Override
  public double convertSolesToDollars(double solesAmount) {
    if (solesAmount < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }

    double exchangeRate = exchangeRateRepository.getCurrentExchangeRate();
    return solesAmount / exchangeRate;
  }
}