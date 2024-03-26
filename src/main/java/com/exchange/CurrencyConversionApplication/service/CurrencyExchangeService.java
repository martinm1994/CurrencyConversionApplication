package com.exchange.CurrencyConversionApplication.service;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CurrencyExchangeService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyExchangeService.class);

    public BigDecimal getExchangeRate(String sourceCurrency, String targetCurrency) {
        String apiUrl = "https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_TxTDD7zFrMZLalGYIrekeVXqKzRLDolV4prYOskn";
        if (StringUtils.isBlank(sourceCurrency) || StringUtils.isBlank(targetCurrency)) {
            throw new IllegalArgumentException("Source and target currencies must not be null or empty");
        }
        
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
                    logger.error(
                            "Failed to fetch exchange rate. Status code: " + response.getStatusLine().getStatusCode());
                    return null;
                }
            }

        } catch (IOException | JSONException e) {
            logger.error("Failed to connect to external API.");
            return null;
        }
    }
}
