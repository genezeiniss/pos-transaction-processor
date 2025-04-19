package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers.LinePayModifier;
import org.springframework.stereotype.Component;

@Component
public class LinePayTrxProcessor extends TransactionProcessor {

    public LinePayTrxProcessor(LinePayModifier properties) {
        super(properties);
    }
}
