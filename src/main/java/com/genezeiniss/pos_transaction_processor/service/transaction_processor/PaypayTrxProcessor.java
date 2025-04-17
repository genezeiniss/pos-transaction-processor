package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.PaypayProperties;
import org.springframework.stereotype.Component;

@Component
public class PaypayTrxProcessor extends TransactionProcessor {

    public PaypayTrxProcessor(PaypayProperties properties) {
        super(properties);
    }
}
