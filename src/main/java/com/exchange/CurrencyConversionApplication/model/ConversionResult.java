package com.exchange.CurrencyConversionApplication.model;

public class ConversionResult {
    private Double convertedAmount;
    private String transactionId;

    public ConversionResult(Double convertedAmount, String transactionId) {
        this.convertedAmount = convertedAmount;
        this.transactionId = transactionId;
    }

    public Double getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(Double convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
