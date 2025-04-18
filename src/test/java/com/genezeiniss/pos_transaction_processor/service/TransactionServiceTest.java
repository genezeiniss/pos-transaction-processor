package com.genezeiniss.pos_transaction_processor.service;

import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.exception.ValidationException;
import com.genezeiniss.pos_transaction_processor.fixture.TransactionFixture;
import com.genezeiniss.pos_transaction_processor.repository.TransactionMetadataRepository;
import com.genezeiniss.pos_transaction_processor.repository.TransactionRepository;
import com.genezeiniss.pos_transaction_processor.service.transaction_processor.TransactionProcessor;
import com.genezeiniss.pos_transaction_processor.service.transaction_processor.TransactionProcessorFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionProcessorFactory processorFactory;

    @Mock
    private TransactionProcessor transactionProcessor;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMetadataRepository transactionMetadataRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    @DisplayName("process transaction with valid transaction and metadata")
    void processTransaction() {
        var transaction = TransactionFixture.stubTransaction(PaymentMethod.CASH, new BigDecimal("1.0"));
        List<TransactionMetadata> metadata = List.of();

        when(processorFactory.getTransactionProcessor(PaymentMethod.CASH)).thenReturn(transactionProcessor);
        doNothing().when(transactionProcessor).validateTransactionOrException(transaction, metadata);
        doNothing().when(transactionProcessor).processTransaction(transaction);

        transactionService.processTransaction(transaction, metadata);

        verify(processorFactory).getTransactionProcessor(PaymentMethod.CASH);
        verify(transactionProcessor).validateTransactionOrException(transaction, metadata);
        verify(transactionProcessor).processTransaction(transaction);
    }

    @Test
    @DisplayName("process transaction: validation failure")
    void validationFailure() {
        var transaction = TransactionFixture.stubTransaction(PaymentMethod.BANK_TRANSFER, new BigDecimal("1.0"));
        List<TransactionMetadata> metadata = Collections.emptyList();

        when(processorFactory.getTransactionProcessor(PaymentMethod.BANK_TRANSFER)).thenReturn(transactionProcessor);
        doThrow(ValidationException.class)
                .when(transactionProcessor).validateTransactionOrException(transaction, metadata);

        assertThrows(ValidationException.class,
                () -> transactionService.processTransaction(transaction, metadata));

        verify(transactionProcessor, never()).processTransaction(transaction);
        verifyNoInteractions(transactionRepository, transactionMetadataRepository);
    }

    @Test
    @DisplayName("process transaction should persist transaction and metadata successfully")
    void persistTransactionAndMetadata() {
        var transaction = TransactionFixture.stubTransaction(PaymentMethod.CASH_ON_DELIVERY, new BigDecimal("1.0"));
        var metadata = TransactionFixture.stubTransactionMetadata("courier", "yamato");
        var metadataList = List.of(metadata);

        when(processorFactory.getTransactionProcessor(PaymentMethod.CASH_ON_DELIVERY)).thenReturn(transactionProcessor);
        doNothing().when(transactionProcessor).validateTransactionOrException(transaction, metadataList);
        doNothing().when(transactionProcessor).processTransaction(transaction);

        when(transactionRepository.create(transaction)).thenReturn(transaction);
        when(transactionMetadataRepository.create(metadata)).thenReturn(metadata);

        transactionService.processTransaction(transaction, metadataList);

        verify(transactionRepository).create(transaction);
        verify(transactionMetadataRepository).create(metadata);
    }

    @Test
    @DisplayName("process transaction with empty metadata")
    void persistTransactionOnly() {
        var transaction = TransactionFixture.stubTransaction(PaymentMethod.CASH, new BigDecimal("1.0"));
        List<TransactionMetadata> metadata = Collections.emptyList();

        when(processorFactory.getTransactionProcessor(PaymentMethod.CASH)).thenReturn(transactionProcessor);
        doNothing().when(transactionProcessor).validateTransactionOrException(transaction, metadata);
        doNothing().when(transactionProcessor).processTransaction(transaction);

        when(transactionRepository.create(transaction)).thenReturn(transaction);

        transactionService.processTransaction(transaction, metadata);

        verify(transactionRepository).create(transaction);
        verify(transactionMetadataRepository, never()).create(any());
    }
}