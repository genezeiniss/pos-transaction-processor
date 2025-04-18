package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.payment_method_properties.LinePayProperties;
import org.springframework.stereotype.Component;

@Component
public class LinePayTrxProcessor extends TransactionProcessor {

    public LinePayTrxProcessor(LinePayProperties properties) {
        super(properties);
    }
}
