package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers.CashModifier;
import org.springframework.stereotype.Component;

@Component
public class CashTrxProcessor extends TransactionProcessor {

    public CashTrxProcessor(CashModifier properties) {
        super(properties);
    }
}
