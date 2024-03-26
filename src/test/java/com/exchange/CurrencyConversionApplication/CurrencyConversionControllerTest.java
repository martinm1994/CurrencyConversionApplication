package com.exchange.CurrencyConversionApplication;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.exchange.CurrencyConversionApplication.controller.CurrencyConversionController;
import com.exchange.CurrencyConversionApplication.service.CurrencyExchangeService;

@RunWith(SpringRunner.class)
@WebMvcTest(CurrencyConversionController.class)
public class CurrencyConversionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CurrencyExchangeService exchangeService;

    @Test
    public void testAllEndpoints() throws Exception {

        BigDecimal exchangeRate = BigDecimal.valueOf(0.88);
        when(exchangeService.getExchangeRate("USD", "EUR")).thenReturn(exchangeRate);

        mockMvc.perform(post("/convert-currency")
                .contentType("application/json")
                .content("{\"sourceCurrency\":\"USD\",\"targetCurrency\":\"EUR\",\"amount\":100.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.convertedAmount").value("88.0"))
                .andExpect(jsonPath("$.transactionIdentifier").isString());

        mockMvc.perform(get("/filtered")
                .param("transactionIdentifier", "12345"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }
}
