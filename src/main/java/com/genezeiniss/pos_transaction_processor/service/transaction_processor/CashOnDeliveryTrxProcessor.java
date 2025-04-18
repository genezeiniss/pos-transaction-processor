package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.payment_method_properties.CashOnDeliveryProperties;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.utils.ValidatorUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CashOnDeliveryTrxProcessor extends TransactionProcessor {

    public CashOnDeliveryTrxProcessor(CashOnDeliveryProperties properties) {
        super(properties);
    }

    @Override
    protected void validateRequiredFields(List<TransactionMetadata> transactionMetadata, List<String> errors) {
        ValidatorUtils.validateCourier(transactionMetadata, ((CashOnDeliveryProperties) properties).getAllowedCouriers(), errors);
    }
}
