package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers.PointsModifier;
import org.springframework.stereotype.Component;

@Component
public class PointsTrxProcessor extends TransactionProcessor {

    public PointsTrxProcessor(PointsModifier properties) {
        super(properties);
    }
}
