package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers.JcbModifier;
import com.genezeiniss.pos_transaction_processor.utils.ValidatorUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JcbTrxProcessor extends TransactionProcessor {

    public JcbTrxProcessor(JcbModifier properties) {
        super(properties);
    }

    @Override
    protected void validateRequiredFields(List<TransactionMetadata> transactionMetadata, List<String> errors) {
        ValidatorUtils.validateCardLastDigits(transactionMetadata, errors);
    }
}
