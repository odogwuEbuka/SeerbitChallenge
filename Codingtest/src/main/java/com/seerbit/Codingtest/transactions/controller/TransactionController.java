package com.seerbit.Codingtest.transactions.controller;
import com.seerbit.Codingtest.transactions.service.TransactionBuffer;
import com.seerbit.Codingtest.transactions.dto.TransactionRequest;
import com.seerbit.Codingtest.transactions.model.Statistics;
import com.seerbit.Codingtest.transactions.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;

@RestController
public class TransactionController {

    private static final int SECONDS_THRESHOLD = 30;

    @Autowired
    private TransactionBuffer transactionBuffer;

    @PostMapping("/transactions")
    public ResponseEntity<Void> addTransaction(@RequestBody TransactionRequest transactionRequest) {
        try {
            BigDecimal amount = new BigDecimal(transactionRequest.getAmount());
            Instant timestamp = transactionRequest.getTimestamp();
            Transaction transaction = new Transaction(amount, timestamp);

            Instant currentTimestamp = Instant.now();

            // Check if the transaction is within the last 30 seconds
            if (transaction.getTimestamp().isBefore(currentTimestamp.minusSeconds(SECONDS_THRESHOLD))) {
                return ResponseEntity.noContent().build(); // 204 No Content
            }

            transactionBuffer.addTransaction(transaction);

            return ResponseEntity.status(HttpStatus.CREATED).build(); // 201 Created
        } catch (NumberFormatException | DateTimeParseException e) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }


    @GetMapping("/statistics")
    public ResponseEntity<Statistics> getStatistics() {
        try {
            Statistics statistics = transactionBuffer.getStatistics();
            return ResponseEntity.ok(statistics);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }

    @DeleteMapping("/transactions")
    public ResponseEntity<Void> deleteTransactions() {
        transactionBuffer.clear();
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}