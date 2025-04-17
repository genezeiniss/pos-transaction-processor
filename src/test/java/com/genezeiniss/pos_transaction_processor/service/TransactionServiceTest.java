package com.genezeiniss.pos_transaction_processor.service;

import com.genezeiniss.pos_transaction_processor.domain.Transaction;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.exception.ValidationException;
import com.genezeiniss.pos_transaction_processor.fixture.TransactionFixture;
import com.genezeiniss.pos_transaction_processor.service.transaction_processor.TransactionProcessor;
import com.genezeiniss.pos_transaction_processor.service.transaction_processor.TransactionProcessorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionProcessorFactory processorFactory;

    @Mock
    private TransactionProcessor transactionProcessor;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("process transaction with valid transaction")
    void processTransaction() {
        Transaction transaction = TransactionFixture.stubTransaction(PaymentMethod.CASH, 1.0, null);

        when(processorFactory.getTransactionProcessor(PaymentMethod.CASH)).thenReturn(transactionProcessor);
        when(transactionProcessor.validateTransaction(transaction)).thenReturn(Collections.emptyList());

        transactionService.processTransaction(transaction);

        verify(processorFactory).getTransactionProcessor(PaymentMethod.CASH);
        verify(transactionProcessor).validateTransaction(transaction);
        verify(transactionProcessor).processTransaction(transaction);
    }

    @Test
    @DisplayName("process transaction with invalid transaction")
    void processInvalidTransaction() {
        Transaction transaction = TransactionFixture.stubTransaction(PaymentMethod.BANK_TRANSFER, 1.0, null);
        List<String> errors = List.of("Missing required field: bank", "Missing required field: accountNumber");

        when(processorFactory.getTransactionProcessor(PaymentMethod.BANK_TRANSFER)).thenReturn(transactionProcessor);
        when(transactionProcessor.validateTransaction(transaction)).thenReturn(errors);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> transactionService.processTransaction(transaction));

        assertEquals(String.format("%s; %s", errors.get(0), errors.get(1)), exception.getMessage());
        verify(transactionProcessor, never()).processTransaction(transaction);
    }
}
