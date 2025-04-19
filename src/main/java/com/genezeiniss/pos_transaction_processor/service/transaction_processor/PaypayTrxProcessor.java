package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers.PaypayModifier;
import org.springframework.stereotype.Component;

@Component
public class PaypayTrxProcessor extends TransactionProcessor {

    public PaypayTrxProcessor(PaypayModifier properties) {
        super(properties);
    }
}
