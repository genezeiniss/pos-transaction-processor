package com.genezeiniss.pos_transaction_processor.service;

import com.genezeiniss.pos_transaction_processor.domain.Transaction;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.service.transaction_processor.TransactionProcessorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionProcessorFactory transactionProcessorFactory;
    private final PersistenceService persistenceService;

    // todo: add logs - make sure that logs doesn't contain transaction metadata, which can include sensitive data
    public void processTransaction(Transaction transaction, List<TransactionMetadata> metadata) {

        var transactionProcessor = transactionProcessorFactory.getTransactionProcessor(transaction.getPaymentMethod());

        transactionProcessor.validateTransactionOrException(transaction, Optional.ofNullable(metadata).orElse(Collections.emptyList()));
        transactionProcessor.processTransaction(transaction);

        persistenceService.persistTransaction(transaction, metadata);
    }
}
