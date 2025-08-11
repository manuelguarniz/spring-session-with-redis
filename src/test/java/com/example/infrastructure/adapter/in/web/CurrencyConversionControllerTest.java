package com.example.infrastructure.adapter.in.web;

import com.example.application.port.in.CurrencyConversionUseCase;
import com.example.infrastructure.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CurrencyConversionController.class)
@Import(TestSecurityConfig.class)
class CurrencyConversionControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CurrencyConversionUseCase currencyConversionUseCase;

  @Test
  void convertSolesToDollars_ValidAmount_ShouldReturnConversion() throws Exception {
    // Given
    double solesAmount = 38.0;
    double expectedDollars = 10.0;
    when(currencyConversionUseCase.convertSolesToDollars(solesAmount)).thenReturn(expectedDollars);

    // When & Then
    mockMvc.perform(get("/api/currency/convert")
        .param("amount", String.valueOf(solesAmount)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.originalAmount").value(38.0))
        .andExpect(jsonPath("$.originalCurrency").value("PEN"))
        .andExpect(jsonPath("$.convertedAmount").value(10.0))
        .andExpect(jsonPath("$.targetCurrency").value("USD"))
        .andExpect(jsonPath("$.exchangeRate").value(3.8))
        .andExpect(jsonPath("$.message").value("Conversion successful"));
  }

  @Test
  void convertSolesToDollars_ZeroAmount_ShouldReturnZero() throws Exception {
    // Given
    double solesAmount = 0.0;
    when(currencyConversionUseCase.convertSolesToDollars(solesAmount)).thenReturn(0.0);

    // When & Then
    mockMvc.perform(get("/api/currency/convert")
        .param("amount", String.valueOf(solesAmount)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.convertedAmount").value(0.0));
  }

  @Test
  void convertSolesToDollars_NegativeAmount_ShouldReturnBadRequest() throws Exception {
    // Given
    double solesAmount = -10.0;
    when(currencyConversionUseCase.convertSolesToDollars(solesAmount))
        .thenThrow(new IllegalArgumentException("Amount cannot be negative"));

    // When & Then
    mockMvc.perform(get("/api/currency/convert")
        .param("amount", String.valueOf(solesAmount)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Amount cannot be negative"))
        .andExpect(jsonPath("$.message").value("Invalid input provided"));
  }
}