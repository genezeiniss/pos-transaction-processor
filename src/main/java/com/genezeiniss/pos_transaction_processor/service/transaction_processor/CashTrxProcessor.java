package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.CashProperties;
import org.springframework.stereotype.Component;

@Component
public class CashTrxProcessor extends TransactionProcessor {

    public CashTrxProcessor(CashProperties properties) {
        super(properties);
    }
}
