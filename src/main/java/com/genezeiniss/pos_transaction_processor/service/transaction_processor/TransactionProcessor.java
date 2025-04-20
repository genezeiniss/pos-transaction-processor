package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.domain.PriceModifierRange;
import com.genezeiniss.pos_transaction_processor.domain.Transaction;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers.PaymentMethodModifier;
import com.genezeiniss.pos_transaction_processor.exception.ValidationException;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public abstract class TransactionProcessor {

    protected final PaymentMethodModifier properties;

    public void validateTransactionOrException(Transaction transaction,
                                               List<TransactionMetadata> transactionMetadata) {
        List<String> errors = new ArrayList<>();
        validateRequiredFields(transactionMetadata, errors);
        validatePriceModifier(transaction.getPriceModifier(), errors);

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    public void processTransaction(Transaction transaction) {
        applyFinalPrice(transaction);
        applyPoints(transaction, this.properties.getPointsMultiplier());
    }

    protected void validateRequiredFields(List<TransactionMetadata> transactionMetadata, List<String> errors) {
    }

    private void validatePriceModifier(double priceModifier, List<String> errors) {

        PriceModifierRange priceModifierRange = properties.getPriceModifierRange();
        double min = priceModifierRange.getMin();
        double max = priceModifierRange.getMax();

        if (priceModifier < min || priceModifier > max) {
            errors.add(String.format("Invalid price modifier. Expected range: %s to %s", min, max));
        }
    }

    private void applyFinalPrice(Transaction transaction) {
        BigDecimal finalPrice = transaction.getOriginalPrice()
                .multiply(BigDecimal.valueOf(transaction.getPriceModifier()))
                .setScale(2, RoundingMode.HALF_UP);
        transaction.setFinalPrice(finalPrice);
    }

    private void applyPoints(Transaction transaction, double pointsMultiplier) {
        int points = transaction.getOriginalPrice()
                .multiply(BigDecimal.valueOf(pointsMultiplier))
                .setScale(0, RoundingMode.HALF_UP)
                .intValue();
        transaction.setPoints(points);
    }
}
