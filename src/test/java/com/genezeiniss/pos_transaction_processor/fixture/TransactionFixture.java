package com.genezeiniss.pos_transaction_processor.fixture;

import com.genezeiniss.pos_transaction_processor.domain.Transaction;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionFixture {

    public static Transaction stubTransaction(PaymentMethod paymentMethod, double priceModifier) {
        Transaction transaction = new Transaction();
        transaction.setUserId("userId");
        transaction.setCustomerId("customerId");
        transaction.setPrice(new BigDecimal("100.00"));
        transaction.setPriceModifier(priceModifier);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setCreatedAt(LocalDateTime.now());
        return transaction;
    }

    public static TransactionMetadata stubTransactionMetadata(String attribute, String data) {
        TransactionMetadata transactionMetadata = new TransactionMetadata();
        transactionMetadata.setAttribute(attribute);
        transactionMetadata.setData(data);
        return transactionMetadata;
    }
}
