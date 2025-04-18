package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.payment_method_properties.PointsProperties;
import org.springframework.stereotype.Component;

@Component
public class PointsTrxProcessor extends TransactionProcessor {

    public PointsTrxProcessor(PointsProperties properties) {
        super(properties);
    }
}
