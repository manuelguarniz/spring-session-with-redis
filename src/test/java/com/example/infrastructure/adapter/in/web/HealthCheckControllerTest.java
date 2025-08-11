package com.example.infrastructure.adapter.in.web;

import com.example.application.port.in.HealthCheckUseCase;
import com.example.infrastructure.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthCheckController.class)
@Import(TestSecurityConfig.class)
class HealthCheckControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private HealthCheckUseCase healthCheckUseCase;

  @Test
  void health_ShouldReturnOk() throws Exception {
    // Given
    when(healthCheckUseCase.checkHealth()).thenReturn("OK");

    // When & Then
    mockMvc.perform(get("/api/health"))
        .andExpect(status().isOk())
        .andExpect(content().string("OK"));
  }
}