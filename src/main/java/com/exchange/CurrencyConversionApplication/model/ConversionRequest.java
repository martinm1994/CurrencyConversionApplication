package com.exchange.CurrencyConversionApplication.model;

import java.math.BigDecimal;

public class ConversionRequest {
    private BigDecimal amount;
    private String sourceCurrency;
    private String targetCurrency;

    public ConversionRequest(String sourceCurrency, String targetCurrency, BigDecimal amount) {
        this.sourceCurrency = sourceCurrency;
        this.targetCurrency = targetCurrency;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

}
