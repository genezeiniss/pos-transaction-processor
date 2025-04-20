package com.genezeiniss.pos_transaction_processor.service;

import com.genezeiniss.pos_transaction_processor.domain.Transaction;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.repository.TransactionMetadataRepository;
import com.genezeiniss.pos_transaction_processor.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class PersistenceService {

    private final TransactionRepository transactionRepository;
    private final TransactionMetadataRepository transactionMetadataRepository;

    // todo: what should be a behaviour in case of failure?
    //  Consider implementation of retry mechanism / dead letter queue for failed transactions
    @Async
    @Transactional
    public void persistTransaction(Transaction transaction, List<TransactionMetadata> transactionMetadata) {
        var trx = transactionRepository.create(transaction);
        transactionMetadata.forEach(metadata -> {
            metadata.setTransactionId(trx.getId());
            transactionMetadataRepository.create(metadata);
        });
    }
}
