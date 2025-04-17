package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.BankTransferProperties;
import com.genezeiniss.pos_transaction_processor.utils.ValidatorUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BankTransferTrxProcessor extends TransactionProcessor {

    public BankTransferTrxProcessor(BankTransferProperties properties) {
        super(properties);
    }

    @Override
    protected void validateRequiredFields(Map<String, String> additionalInfo, List<String> errors) {
        ValidatorUtils.validateBank(additionalInfo, errors);
        ValidatorUtils.validateAccountNumber(additionalInfo, errors);
    }
}
