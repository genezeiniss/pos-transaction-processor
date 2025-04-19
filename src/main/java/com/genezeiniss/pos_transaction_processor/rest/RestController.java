package com.genezeiniss.pos_transaction_processor.rest;

import com.genezeiniss.pos_transaction_processor.domain.Transaction;
import com.genezeiniss.pos_transaction_processor.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@AllArgsConstructor
@org.springframework.web.bind.annotation.RestController
public class RestController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> generateListingData(@RequestBody Transaction transaction) {
        transactionService.processTransaction(transaction, null);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }
}
