package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.payment_method_properties.JcbProperties;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.utils.ValidatorUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JcbTrxProcessor extends TransactionProcessor {

    public JcbTrxProcessor(JcbProperties properties) {
        super(properties);
    }

    @Override
    protected void validateRequiredFields(List<TransactionMetadata> transactionMetadata, List<String> errors) {
        ValidatorUtils.validateCardLastDigits(transactionMetadata, errors);
    }
}
