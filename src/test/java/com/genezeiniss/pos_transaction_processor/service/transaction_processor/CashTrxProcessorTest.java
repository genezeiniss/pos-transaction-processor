package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.domain.PriceModifierRange;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers.CashModifier;
import com.genezeiniss.pos_transaction_processor.exception.ValidationException;
import com.genezeiniss.pos_transaction_processor.fixture.TransactionFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CashTrxProcessorTest {

    private static CashTrxProcessor transactionProcessor;
    private final PaymentMethod paymentMethod = PaymentMethod.CASH;

    @BeforeAll
    static void setup() {
        CashModifier properties = new CashModifier();
        properties.setPointsMultiplier(0.05);
        properties.setPriceModifierRange(new PriceModifierRange(0.9, 1.0));

        transactionProcessor = new CashTrxProcessor(properties);
    }

    @Test
    public void processTransaction() {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, 0.95);
        transactionProcessor.processTransaction(transaction);

        assertEquals(new BigDecimal("95.00"), transaction.getFinalPrice(), "final price");
        assertEquals(5, transaction.getPoints(), "points");
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.89, 1.01, 0})
    @DisplayName("validate transaction: invalid price modifier")
    public void invalidPriceModifier(double priceModifier) {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, priceModifier);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> transactionProcessor.validateTransactionOrException(transaction, List.of()));

        assertEquals("Invalid price modifier. Expected range: 0.9 to 1.0", exception.getMessage());
    }
}
