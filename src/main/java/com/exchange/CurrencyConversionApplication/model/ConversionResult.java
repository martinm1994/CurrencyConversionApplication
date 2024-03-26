package com.exchange.CurrencyConversionApplication.model;

import java.math.BigDecimal;

public class ConversionResult {
    private BigDecimal convertedAmount;
    private String transactionId;

    public ConversionResult(BigDecimal convertedAmount, String transactionId) {
        this.convertedAmount = convertedAmount;
        this.transactionId = transactionId;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
