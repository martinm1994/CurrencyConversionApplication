package com.exchange.CurrencyConversionApplication.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.exchange.CurrencyConversionApplication.model.Transaction;
import com.exchange.CurrencyConversionApplication.repository.TransactionRepository;

@Service
public class CurrencyConversionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public BigDecimal getConvertedAmount(String sourceCurrency, String targetCurrency, BigDecimal amount) {
        String apiUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_TxTDD7zFrMZLalGYIrekeVXqKzRLDolV4prYOskn";
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String url = apiUrl + "&base=" + sourceCurrency.toUpperCase();

            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    String json = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = new JSONObject(json);

                    JSONObject rates = jsonObject.getJSONObject("data");
                    BigDecimal rate = rates.getBigDecimal(targetCurrency.toUpperCase());

                    return rate;
                } else {
                    System.err.println(
                            "Failed to fetch exchange rate. Status code: " + response.getStatusLine().getStatusCode());
                    return null;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Page<Transaction> getFilteredTransactions(String transactionIdentifier, LocalDate transactionDate,
            Pageable pageable) {
        if (transactionIdentifier != null) {
            return transactionRepository.findByTransactionIdentifier(transactionIdentifier, pageable);
        } else if (transactionDate != null) {
            return transactionRepository.findByTransactionDate(transactionDate, pageable);
        } else {
            throw new IllegalArgumentException(
                    "At least one of transaction identifier or transaction date must be provided!");
        }
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
