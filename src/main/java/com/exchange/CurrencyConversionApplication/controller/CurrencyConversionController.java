package com.exchange.CurrencyConversionApplication.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exchange.CurrencyConversionApplication.model.ConversionRequest;
import com.exchange.CurrencyConversionApplication.model.Transaction;
import com.exchange.CurrencyConversionApplication.repository.TransactionRepository;
import com.exchange.CurrencyConversionApplication.service.CurrencyConversionService;
import com.exchange.CurrencyConversionApplication.service.CurrencyExchangeService;

@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeService exchangeService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CurrencyConversionService conversionService;

    @GetMapping("/exchange-rate")
    public ResponseEntity<Map<String, BigDecimal>> getExchangeRate(@RequestParam String sourceCurrency,
            @RequestParam String targetCurrency) {
        BigDecimal rate = exchangeService.getExchangeRate(sourceCurrency, targetCurrency);
        Map<String, BigDecimal> response = new HashMap<>();

        response.put("exchangeRate", rate);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/convert-currency")
    public ResponseEntity<Map<String, String>> convertCurrency(@RequestBody ConversionRequest request) {
        BigDecimal exchangeRate = exchangeService.getExchangeRate(request.getSourceCurrency(),
                request.getTargetCurrency());
        if (exchangeRate == null) {
            System.err.println("Failed to fetch exchange rate");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        BigDecimal convertedAmount = request.getAmount().multiply(exchangeRate);
        String transactionIdentifier = UUID.randomUUID().toString();
        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setSourceCurrency(request.getSourceCurrency());
        transaction.setTargetCurrency(request.getTargetCurrency());
        transaction.setConvertedAmount(convertedAmount);
        transaction.setTransactionIdentifier(transactionIdentifier);
        transaction.setTransactionDate(LocalDate.now());

        @SuppressWarnings("unused")
        Transaction savedTransaction = transactionRepository.save(transaction);
        Map<String, String> response = new HashMap<>();
        response.put("convertedAmount", convertedAmount.toString());
        response.put("transactionIdentifier", transactionIdentifier);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/filtered")
    public ResponseEntity<Page<Transaction>> getFilteredTransactions(
            @RequestParam(required = false) String transactionIdentifier,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate transactionDate,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Transaction> transactions = conversionService.getFilteredTransactions(transactionIdentifier,
                transactionDate, pageable);
        return ResponseEntity.ok().body(transactions);
    }
}
