package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.CashOnDeliveryProperties;
import com.genezeiniss.pos_transaction_processor.utils.ValidatorUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CashOnDeliveryTrxProcessor extends TransactionProcessor {

    public CashOnDeliveryTrxProcessor(CashOnDeliveryProperties properties) {
        super(properties);
    }

    @Override
    protected void validateRequiredFields(Map<String, String> additionalInfo, List<String> errors) {
        ValidatorUtils.validateCourier(additionalInfo, ((CashOnDeliveryProperties) properties).getAllowedCouriers(), errors);
    }
}
