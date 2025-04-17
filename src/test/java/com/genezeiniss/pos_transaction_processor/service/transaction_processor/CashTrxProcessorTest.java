package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.CashProperties;
import com.genezeiniss.pos_transaction_processor.domain.PriceModifierRange;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.fixture.TransactionFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CashTrxProcessorTest {

    private static CashTrxProcessor transactionProcessor;
    private final PaymentMethod paymentMethod = PaymentMethod.CASH;

    @BeforeAll
    static void setup() {
        CashProperties properties = new CashProperties();
        properties.setPointsMultiplier(0.05);
        properties.setPriceModifierRange(new PriceModifierRange(0.9, 1.0));

        transactionProcessor = new CashTrxProcessor(properties);
    }

    @Test
    public void processTransaction() {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, 0.95, null);
        transactionProcessor.processTransaction(transaction);

        assertEquals(95, transaction.getFinalPrice(), "final price");
        assertEquals(5, transaction.getPoints(), "points");
    }


    @ParameterizedTest
    @ValueSource(doubles = {0.89, 1.01, 0})
    @DisplayName("validate transaction: invalid price modifier")
    public void invalidPriceModifier(double priceModifier) {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, priceModifier, Map.of("courier", "courier1"));
        List<String> errors = transactionProcessor.validateTransaction(transaction);

        assertEquals(1, errors.size(), "number of errors");
        assertEquals("Invalid price modifier. Expected range: 0.9 to 1.0", errors.getFirst(), "error message");
    }
}
