package com.genezeiniss.pos_transaction_processor.service;

import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.fixture.TransactionFixture;
import com.genezeiniss.pos_transaction_processor.repository.TransactionMetadataRepository;
import com.genezeiniss.pos_transaction_processor.repository.TransactionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersistenceServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMetadataRepository transactionMetadataRepository;

    @InjectMocks
    private PersistenceService persistenceService;

    @Test
    @DisplayName("persist transaction should persist transaction and metadata successfully")
    void persistTransactionAndMetadata() {
        var transaction = TransactionFixture.stubTransaction(PaymentMethod.CASH_ON_DELIVERY, 1.0);
        var metadata = TransactionFixture.stubTransactionMetadata("courier", "yamato");
        var transactionMetadata = List.of(TransactionFixture.stubTransactionMetadata("courier", "yamato"));

        when(transactionRepository.create(transaction)).thenReturn(transaction);
        when(transactionMetadataRepository.create(metadata)).thenReturn(metadata);

        persistenceService.persistTransaction(transaction, transactionMetadata);

        verify(transactionRepository).create(transaction);
        verify(transactionMetadataRepository).create(metadata);
    }

    @Test
    @DisplayName("persist transaction with empty metadata")
    void persistTransactionOnly() {
        var transaction = TransactionFixture.stubTransaction(PaymentMethod.CASH, 1.0);
        List<TransactionMetadata> transactionMetadata = Collections.emptyList();

        when(transactionRepository.create(transaction)).thenReturn(transaction);

        persistenceService.persistTransaction(transaction, transactionMetadata);

        verify(transactionRepository).create(transaction);
        verify(transactionMetadataRepository, never()).create(any());
    }
}