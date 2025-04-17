package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.PaymentMethodProperties;
import com.genezeiniss.pos_transaction_processor.domain.PriceModifierRange;
import com.genezeiniss.pos_transaction_processor.domain.Transaction;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public abstract class TransactionProcessor {

    protected final PaymentMethodProperties properties;

    public List<String> validateTransaction(Transaction transaction) {
        List<String> errors = new ArrayList<>();
        validateRequiredFields(transaction.getAdditionalInformation(), errors);
        validatePriceModifier(transaction.getPriceModifier(), errors);
        return errors;
    }

    public void processTransaction(Transaction transaction) {
        applyFinalPrice(transaction);
        applyPoints(transaction, this.properties.getPointsMultiplier());
    }

    protected void validateRequiredFields(Map<String, String> additionalInfo, List<String> errors) {
    }

    private void validatePriceModifier(double priceModifier, List<String> errors) {
        PriceModifierRange priceModifierRange = properties.getPriceModifierRange();
        if (priceModifier < priceModifierRange.min() || priceModifier > priceModifierRange.max()) {
            errors.add(String.format(
                    "Invalid price modifier. Expected range: %s to %s", priceModifierRange.min(), priceModifierRange.max()));
        }
    }

    private void applyFinalPrice(Transaction transaction) {
        double finalPrice = transaction.getPrice() * transaction.getPriceModifier();
        transaction.setFinalPrice(finalPrice);
    }

    private void applyPoints(Transaction transaction, double pointsMultiplier) {
        transaction.setPoints((int) (transaction.getPrice() * pointsMultiplier));
    }
}
