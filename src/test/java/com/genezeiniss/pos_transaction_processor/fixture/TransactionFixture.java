package com.genezeiniss.pos_transaction_processor.fixture;

import com.genezeiniss.pos_transaction_processor.domain.Transaction;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public class TransactionFixture {

    public static Transaction stubTransaction(PaymentMethod paymentMethod,
                                              BigDecimal priceModifier,
                                              Map<String, String> additionalInfo) {
        Transaction transaction = new Transaction();
        transaction.setUserId("userId");
        transaction.setCustomerId("customerId");
        transaction.setPrice(new BigDecimal("100"));
        transaction.setPriceModifier(priceModifier);
        transaction.setPaymentMethod(paymentMethod);
        transaction.setDatetime(Instant.now());
        transaction.setAdditionalInformation(additionalInfo);
        return transaction;
    }
}
