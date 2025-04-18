package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.payment_method_properties.AmexProperties;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.utils.ValidatorUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AmexTrxProcessor extends TransactionProcessor {

    public AmexTrxProcessor(AmexProperties properties) {
        super(properties);
    }

    @Override
    protected void validateRequiredFields(List<TransactionMetadata> transactionMetadata, List<String> errors) {
        ValidatorUtils.validateCardLastDigits(transactionMetadata, errors);
    }
}
