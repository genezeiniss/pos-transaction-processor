package com.genezeiniss.pos_transaction_processor.fixture;

import com.genezeiniss.pos_transaction_processor.domain.Transaction;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class TransactionFixture {

    public static Transaction stubTransaction(PaymentMethod paymentMethod, double priceModifier) {
        var transaction = new Transaction();
        transaction.setCustomerId("customerId");
        transaction.setOriginalPrice(new BigDecimal("100.00"));
        transaction.setPriceModifier(priceModifier);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setCreatedAt(OffsetDateTime.now());
        return transaction;
    }

    public static TransactionMetadata stubTransactionMetadata(String attribute, String data) {
        var transactionMetadata = new TransactionMetadata();
        transactionMetadata.setAttribute(attribute);
        transactionMetadata.setData(data);
        return transactionMetadata;
    }
}
