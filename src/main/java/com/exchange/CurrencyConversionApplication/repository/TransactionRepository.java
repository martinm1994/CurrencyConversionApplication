package com.exchange.CurrencyConversionApplication.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.exchange.CurrencyConversionApplication.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByTransactionIdentifier(String transactionIdentifier, Pageable pageable);

    Page<Transaction> findByTransactionDate(LocalDate transactionDate, Pageable pageable);

    Page<Transaction> findById(Long id, Pageable pageable);

}
