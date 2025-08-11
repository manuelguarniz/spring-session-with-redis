package com.example.application.port.in;

/**
 * Input port for currency conversion use case
 * This interface defines the contract for converting Peruvian Soles to US
 * Dollars
 */
public interface CurrencyConversionUseCase {

  /**
   * Converts Peruvian Soles (PEN) to US Dollars (USD)
   * 
   * @param solesAmount amount in Peruvian Soles to convert
   * @return converted amount in US Dollars
   */
  double convertSolesToDollars(double solesAmount);
}