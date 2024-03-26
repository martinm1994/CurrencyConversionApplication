package com.exchange.CurrencyConversionApplication;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import com.exchange.CurrencyConversionApplication.controller.CurrencyConversionController;
import com.exchange.CurrencyConversionApplication.service.CurrencyConversionService;
import com.exchange.CurrencyConversionApplication.service.CurrencyExchangeService;

@WebMvcTest(CurrencyConversionController.class)
public class CurrencyConversionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CurrencyConversionService conversionService;

    @Mock
    private CurrencyConversionController conversionController;

    @Mock
    private CurrencyExchangeService exchangeService;

    public void testAllEndpoints() throws Exception {
        // Mock exchange service response
        BigDecimal exchangeRate = BigDecimal.valueOf(0.88);
        when(exchangeService.getExchangeRate("USD", "EUR")).thenReturn(exchangeRate);

        // Mock transaction repository behavior

        // Test convertCurrency endpoint
        mockMvc.perform(post("/convert-currency")
                .contentType("application/json")
                .content("{\"sourceCurrency\":\"USD\",\"targetCurrency\":\"EUR\",\"amount\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.convertedAmount").value("88.0"))
                .andExpect(jsonPath("$.transactionIdentifier").isString());

        // Test filtered endpoint
        mockMvc.perform(get("/filtered")
                .param("transactionIdentifier", "12345"))
                .andExpect(status().isOk());
    }
}
