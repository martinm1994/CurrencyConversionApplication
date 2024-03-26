package com.exchange.CurrencyConversionApplication.service;

import java.math.BigDecimal;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class CurrencyExchangeService {
    public BigDecimal getExchangeRate(String sourceCurrency, String targetCurrency) {
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
}
