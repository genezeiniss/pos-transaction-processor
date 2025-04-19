package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers.GrabPayModifier;
import org.springframework.stereotype.Component;

@Component
public class GrabPayTrxProcessor extends TransactionProcessor {

    public GrabPayTrxProcessor(GrabPayModifier properties) {
        super(properties);
    }
}
