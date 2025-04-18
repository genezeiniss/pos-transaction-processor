package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.CashOnDeliveryProperties;
import com.genezeiniss.pos_transaction_processor.domain.PriceModifierRange;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.fixture.TransactionFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CashOnDeliveryTrxProcessorTest {

    private static CashOnDeliveryTrxProcessor transactionProcessor;
    private final PaymentMethod paymentMethod = PaymentMethod.CASH_ON_DELIVERY;

    @BeforeAll
    static void setup() {
        CashOnDeliveryProperties properties = new CashOnDeliveryProperties();
        properties.setPointsMultiplier(new BigDecimal("0.05"));
        properties.setPriceModifierRange(new PriceModifierRange(new BigDecimal("1.0"), new BigDecimal("1.02")));
        properties.setAllowedCouriers(List.of("courier1", "courier2"));

        transactionProcessor = new CashOnDeliveryTrxProcessor(properties);
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("additional info is missing",
                        null,
                        "Missing required field: courier"),
                Arguments.of("courier is missing",
                        Map.of(),
                        "Missing required field: courier"),
                Arguments.of("courier is blank",
                        Map.of("courier", ""),
                        "Missing required field: courier"),
                Arguments.of("courier does not accept payment method",
                        Map.of("courier", "courier3"),
                        "Courier courier3 does not accept this payment method"));
    }

    @ParameterizedTest
    @MethodSource("arguments")
    @DisplayName("validate transaction with invalid required fields")
    public void validationFailure(String scenario, Map<String, String> additionalInfo, String expectedError) {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, new BigDecimal("1.0"), additionalInfo);
        List<String> errors = transactionProcessor.validateTransaction(transaction);

        assertEquals(1, errors.size(), "number of errors");
        assertEquals(expectedError, errors.getFirst());
    }

    @Test
    @DisplayName("validate transaction: happy flow")
    public void validateTransaction() {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, new BigDecimal("1.01"), Map.of("courier", "courier1"));
        List<String> errors = transactionProcessor.validateTransaction(transaction);
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("process transaction: happy flow")
    public void processTransaction() {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, new BigDecimal("1.01"), Map.of("courier", "courier1"));
        transactionProcessor.processTransaction(transaction);

        assertEquals(new BigDecimal("101.00"), transaction.getFinalPrice(), "final price");
        assertEquals(5, transaction.getPoints(), "points");
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.99, 1.03, 1.1})
    @DisplayName("validate transaction: invalid price modifier")
    public void invalidPriceModifier(double priceModifier) {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, BigDecimal.valueOf(priceModifier), Map.of("courier", "courier1"));
        List<String> errors = transactionProcessor.validateTransaction(transaction);

        assertEquals(1, errors.size(), "number of errors");
        assertEquals("Invalid price modifier. Expected range: 1.0 to 1.02", errors.getFirst(), "error message");
    }
}
