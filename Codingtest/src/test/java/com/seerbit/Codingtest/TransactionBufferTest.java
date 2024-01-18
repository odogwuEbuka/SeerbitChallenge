package com.seerbit.Codingtest;

import com.seerbit.Codingtest.transactions.model.Statistics;
import com.seerbit.Codingtest.transactions.model.Transaction;
import com.seerbit.Codingtest.transactions.service.TransactionBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionBufferTest {

    private TransactionBuffer transactionBuffer;

    @BeforeEach
    void setUp() {
        transactionBuffer = new TransactionBuffer();
    }

    @Test
    void addTransaction_validTransaction_addedToBuffer() {
        Transaction transaction = new Transaction(BigDecimal.valueOf(10), Instant.now());

        transactionBuffer.addTransaction(transaction);

        assertEquals(1, transactionBuffer.getCount());
    }

    @Test
    void getStatistics_noTransactions_returnsEmptyStatistics() {
        Statistics statistics = transactionBuffer.getStatistics();

        assertEquals(BigDecimal.ZERO, statistics.getSum());
        assertEquals(BigDecimal.ZERO, statistics.getAvg());
        assertEquals(BigDecimal.ZERO, statistics.getMax());
        assertEquals(BigDecimal.ZERO, statistics.getMin());
        assertEquals(0, statistics.getCount());
    }



    @Test
    void clear_transactionsCleared_bufferIsEmpty() {
        // Add transactions to the buffer
        transactionBuffer.addTransaction(new Transaction(BigDecimal.valueOf(10), Instant.now()));
        transactionBuffer.addTransaction(new Transaction(BigDecimal.valueOf(20), Instant.now()));
        transactionBuffer.addTransaction(new Transaction(BigDecimal.valueOf(30), Instant.now()));

        transactionBuffer.clear();

        assertEquals(0, transactionBuffer.getCount());
    }
}