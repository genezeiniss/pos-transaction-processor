package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.MastercardProperties;
import com.genezeiniss.pos_transaction_processor.utils.ValidatorUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class MastercardTrxProcessor extends TransactionProcessor {

    public MastercardTrxProcessor(MastercardProperties properties) {
        super(properties);
    }

    @Override
    protected void validateRequiredFields(Map<String, String> additionalInfo, List<String> errors) {
        ValidatorUtils.validateCardLastDigits(additionalInfo, errors);
    }
}
