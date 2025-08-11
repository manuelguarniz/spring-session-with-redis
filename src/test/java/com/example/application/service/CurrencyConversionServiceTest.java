package com.example.application.service;

import com.example.application.port.out.ExchangeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyConversionServiceTest {

  @Mock
  private ExchangeRateRepository exchangeRateRepository;

  private CurrencyConversionService currencyConversionService;

  @BeforeEach
  void setUp() {
    currencyConversionService = new CurrencyConversionService(exchangeRateRepository);
  }

  @Test
  void convertSolesToDollars_ValidAmount_ShouldReturnCorrectConversion() {
    // Given
    double solesAmount = 38.0;
    double exchangeRate = 3.8;
    double expectedDollars = 10.0;

    when(exchangeRateRepository.getCurrentExchangeRate()).thenReturn(exchangeRate);

    // When
    double result = currencyConversionService.convertSolesToDollars(solesAmount);

    // Then
    assertEquals(expectedDollars, result, 0.01);
  }

  @Test
  void convertSolesToDollars_ZeroAmount_ShouldReturnZero() {
    // Given
    double solesAmount = 0.0;
    double exchangeRate = 3.8;

    when(exchangeRateRepository.getCurrentExchangeRate()).thenReturn(exchangeRate);

    // When
    double result = currencyConversionService.convertSolesToDollars(solesAmount);

    // Then
    assertEquals(0.0, result, 0.01);
  }

  @Test
  void convertSolesToDollars_NegativeAmount_ShouldThrowException() {
    // Given
    double solesAmount = -10.0;

    // When & Then
    IllegalArgumentException exception = assertThrows(
        IllegalArgumentException.class,
        () -> currencyConversionService.convertSolesToDollars(solesAmount));

    assertEquals("Amount cannot be negative", exception.getMessage());
  }

  @Test
  void convertSolesToDollars_DecimalAmount_ShouldReturnCorrectConversion() {
    // Given
    double solesAmount = 19.0;
    double exchangeRate = 3.8;
    double expectedDollars = 5.0;

    when(exchangeRateRepository.getCurrentExchangeRate()).thenReturn(exchangeRate);

    // When
    double result = currencyConversionService.convertSolesToDollars(solesAmount);

    // Then
    assertEquals(expectedDollars, result, 0.01);
  }
}