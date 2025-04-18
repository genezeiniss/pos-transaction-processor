package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.PaymentMethodProperties;
import com.genezeiniss.pos_transaction_processor.domain.PriceModifierRange;
import com.genezeiniss.pos_transaction_processor.domain.Transaction;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    private void validatePriceModifier(BigDecimal priceModifier, List<String> errors) {
        PriceModifierRange priceModifierRange = properties.getPriceModifierRange();
        if (priceModifier.compareTo(priceModifierRange.min()) < 0 ||
                priceModifier.compareTo(priceModifierRange.max()) > 0) {
            errors.add(String.format(
                    "Invalid price modifier. Expected range: %s to %s", priceModifierRange.min(), priceModifierRange.max()));
        }
    }

    private void applyFinalPrice(Transaction transaction) {
        BigDecimal finalPrice = transaction.getPrice()
                .multiply(transaction.getPriceModifier())
                .setScale(2, RoundingMode.HALF_UP);
        transaction.setFinalPrice(finalPrice);
    }

    private void applyPoints(Transaction transaction, BigDecimal pointsMultiplier) {
        int points = transaction.getPrice().multiply(pointsMultiplier)
                .setScale(0, RoundingMode.HALF_UP).intValue();
        transaction.setPoints(points);
    }
}
