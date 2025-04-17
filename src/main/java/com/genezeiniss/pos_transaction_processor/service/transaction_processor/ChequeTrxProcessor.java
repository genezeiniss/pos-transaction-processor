package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.ChequeProperties;
import com.genezeiniss.pos_transaction_processor.utils.ValidatorUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ChequeTrxProcessor extends TransactionProcessor {

    public ChequeTrxProcessor(ChequeProperties properties) {
        super(properties);
    }

    @Override
    protected void validateRequiredFields(Map<String, String> additionalInfo, List<String> errors) {
        ValidatorUtils.validateBank(additionalInfo, errors);
        ValidatorUtils.validateChequeNumber(additionalInfo, errors);
    }
}
