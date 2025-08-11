package com.example.infrastructure.adapter.in.web;

import com.example.application.port.in.CurrencyConversionUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Web controller for currency conversion endpoints
 * This class handles HTTP requests for currency conversion functionality
 */
@RestController
@RequestMapping("/api/currency")
public class CurrencyConversionController {

  private final CurrencyConversionUseCase currencyConversionUseCase;

  public CurrencyConversionController(CurrencyConversionUseCase currencyConversionUseCase) {
    this.currencyConversionUseCase = currencyConversionUseCase;
  }

  /**
   * Currency conversion endpoint from PEN to USD
   * 
   * @param solesAmount amount in Peruvian Soles to convert
   * @return conversion result with details
   */
  @GetMapping("/convert")
  public ResponseEntity<Map<String, Object>> convertSolesToDollars(
      @RequestParam("amount") double solesAmount) {

    try {
      double dollarsAmount = currencyConversionUseCase.convertSolesToDollars(solesAmount);

      Map<String, Object> response = new HashMap<>();
      response.put("originalAmount", solesAmount);
      response.put("originalCurrency", "PEN");
      response.put("convertedAmount", Math.round(dollarsAmount * 100.0) / 100.0);
      response.put("targetCurrency", "USD");
      response.put("exchangeRate", 3.8);
      response.put("message", "Conversion successful");

      return ResponseEntity.ok(response);

    } catch (IllegalArgumentException e) {
      Map<String, Object> errorResponse = new HashMap<>();
      errorResponse.put("error", e.getMessage());
      errorResponse.put("message", "Invalid input provided");

      return ResponseEntity.badRequest().body(errorResponse);
    }
  }
}