package com.genezeiniss.pos_transaction_processor.service;

import com.genezeiniss.pos_transaction_processor.domain.Transaction;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.repository.TransactionMetadataRepository;
import com.genezeiniss.pos_transaction_processor.repository.TransactionRepository;
import com.genezeiniss.pos_transaction_processor.service.transaction_processor.TransactionProcessorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionProcessorFactory transactionProcessorFactory;
    private final TransactionRepository transactionRepository;
    private final TransactionMetadataRepository transactionMetadataRepository;

    // todo: add logs
    public void processTransaction(Transaction transaction, List<TransactionMetadata> metadata) {

        var transactionProcessor = transactionProcessorFactory.getTransactionProcessor(transaction.getPaymentMethod());

        transactionProcessor.validateTransactionOrException(transaction, Optional.ofNullable(metadata).orElse(Collections.emptyList()));
        transactionProcessor.processTransaction(transaction);

        persistTransaction(transaction, metadata);
    }

    @Transactional
    private void persistTransaction(Transaction transaction, List<TransactionMetadata> transactionMetadata) {
        var trx = transactionRepository.create(transaction);
        transactionMetadata.forEach(metadata -> {
            metadata.setTransactionId(trx.getId());
            transactionMetadataRepository.create(metadata);
        });
    }
}
