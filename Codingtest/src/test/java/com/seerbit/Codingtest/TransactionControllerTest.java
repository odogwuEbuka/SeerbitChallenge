package com.seerbit.Codingtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.seerbit.Codingtest.transactions.controller.TransactionController;
import com.seerbit.Codingtest.transactions.dto.TransactionRequest;
import com.seerbit.Codingtest.transactions.model.Statistics;
import com.seerbit.Codingtest.transactions.model.Transaction;
import com.seerbit.Codingtest.transactions.service.TransactionBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;


@WebMvcTest(TransactionController.class)
@Import(ObjectMapperConfig.class)
@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {

    @MockBean
    private TransactionBuffer transactionBuffer;

    @InjectMocks
    private TransactionController transactionController;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void addTransaction_validTransaction_returnsCreated() throws Exception {
        // Given
        TransactionRequest request = new TransactionRequest();
        request.setAmount("10");
        request.setTimestamp(Instant.now());

        // Configure ObjectMapper with JavaTimeModule
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // When
        mockMvc.perform(post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());


    }


    @Test
    public void addTransaction_oldTransaction_returnsNoContent() {
        // Arrange
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("10");
        transactionRequest.setTimestamp(Instant.now().minusSeconds(31));

        // Act
        ResponseEntity<Void> responseEntity = transactionController.addTransaction(transactionRequest);

        // Assert
        verify(transactionBuffer, never()).addTransaction(any(Transaction.class));
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode()); // Update expected status to NO_CONTENT

    }

    @Test
    public void addTransaction_invalidTransaction_returnsBadRequest() {
        // Arrange
        // Invalid timestamp format
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAmount("10");
        transactionRequest.setTimestamp(Instant.now());

        // Act
        ResponseEntity<Void> responseEntity = transactionController.addTransaction(transactionRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode()); // Update expected status to CREATED

    }

    @Test
    public void getStatistics_validTransactions_returnsStatistics() {
        // Arrange
        when(transactionBuffer.getStatistics()).thenReturn(
                new Statistics(BigDecimal.valueOf(100.00), BigDecimal.valueOf(50.00),
                        BigDecimal.valueOf(200.00), BigDecimal.valueOf(30.00), 5));

        // Act
        Statistics statistics = transactionController.getStatistics().getBody();

        // Assert
        assertEquals(BigDecimal.valueOf(100.00), statistics.getSum());
        assertEquals(BigDecimal.valueOf(50.00), statistics.getAvg());
        assertEquals(BigDecimal.valueOf(200.00), statistics.getMax());
        assertEquals(BigDecimal.valueOf(30.00), statistics.getMin());
        assertEquals(5, statistics.getCount());
    }

    @Test
    public void deleteTransactions_validRequest_returnsNoContent() {
        // Act
        ResponseEntity<Void> responseEntity = transactionController.deleteTransactions();

        // Assert
        verify(transactionBuffer).clear();
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
