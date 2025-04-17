package com.genezeiniss.pos_transaction_processor.service;

import com.genezeiniss.pos_transaction_processor.domain.Transaction;
import com.genezeiniss.pos_transaction_processor.exception.ValidationException;
import com.genezeiniss.pos_transaction_processor.service.transaction_processor.TransactionProcessorFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    // todo: add repository
    private final TransactionProcessorFactory transactionProcessorFactory;

    public void processTransaction(Transaction transaction) {
        var transactionProcessor = transactionProcessorFactory.getTransactionProcessor(transaction.getPaymentMethod());
        List<String> errors = transactionProcessor.validateTransaction(transaction);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        transactionProcessor.processTransaction(transaction);
    }
}
