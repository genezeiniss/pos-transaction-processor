package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers.CashOnDeliveryModifier;
import com.genezeiniss.pos_transaction_processor.utils.ValidatorUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CashOnDeliveryTrxProcessor extends TransactionProcessor {

    public CashOnDeliveryTrxProcessor(CashOnDeliveryModifier properties) {
        super(properties);
    }

    @Override
    protected void validateRequiredFields(List<TransactionMetadata> transactionMetadata, List<String> errors) {
        ValidatorUtils.validateCourier(transactionMetadata, ((CashOnDeliveryModifier) properties).getAllowedCouriers(), errors);
    }
}
