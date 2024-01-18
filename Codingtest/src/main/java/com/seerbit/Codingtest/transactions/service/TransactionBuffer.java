package com.seerbit.Codingtest.transactions.service;

import com.seerbit.Codingtest.transactions.model.Statistics;
import com.seerbit.Codingtest.transactions.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TransactionBuffer {
    private final Map<Long, Transaction> transactions = new ConcurrentHashMap<>();
    private static final int SECONDS_THRESHOLD = 30;

    public synchronized void addTransaction(Transaction transaction) {
        transactions.put(transaction.getTimestamp().toEpochMilli(), transaction);
    }

    public synchronized Statistics getStatistics() {
        Instant currentTimestamp = Instant.now();
        BigDecimal sum = BigDecimal.ZERO;
        BigDecimal max = BigDecimal.ZERO;
        BigDecimal min = transactions.isEmpty() ? BigDecimal.ZERO : BigDecimal.valueOf(Double.MAX_VALUE);

        // Include transactions from the map
        for (Transaction transaction : transactions.values()) {
            if (transaction.getTimestamp().isAfter(currentTimestamp.minusSeconds(SECONDS_THRESHOLD))) {
                sum = sum.add(transaction.getAmount());
                max = max.max(transaction.getAmount());
                min = min.min(transaction.getAmount());
            }
        }

        // Include transactions within the 30-second threshold
        long count = transactions.values().stream()
                .filter(transaction -> transaction.getTimestamp().isAfter(currentTimestamp.minusSeconds(SECONDS_THRESHOLD)))
                .count();

        BigDecimal avg = count == 0 ? BigDecimal.ZERO : sum.divide(BigDecimal.valueOf(count), 2, BigDecimal.ROUND_HALF_UP);

        return new Statistics(sum, avg, max, min, count);
    }

    public synchronized void clear() {
        transactions.clear();
    }

    public synchronized int getCount() {
        return transactions.size();
    }
}