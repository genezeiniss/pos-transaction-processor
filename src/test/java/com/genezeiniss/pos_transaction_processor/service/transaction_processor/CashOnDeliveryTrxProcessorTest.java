package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.domain.PriceModifierRange;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers.CashOnDeliveryModifier;
import com.genezeiniss.pos_transaction_processor.exception.ValidationException;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CashOnDeliveryTrxProcessorTest {

    private static CashOnDeliveryTrxProcessor transactionProcessor;
    private final PaymentMethod paymentMethod = PaymentMethod.CASH_ON_DELIVERY;

    @BeforeAll
    static void setup() {
        CashOnDeliveryModifier properties = new CashOnDeliveryModifier();
        properties.setPointsMultiplier(0.05);
        properties.setPriceModifierRange(new PriceModifierRange(1.0, 1.02));
        properties.setAllowedCouriers(List.of("courier1", "courier2"));

        transactionProcessor = new CashOnDeliveryTrxProcessor(properties);
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("additional info is missing",
                        null,
                        "Missing required field: courier"),
                Arguments.of("courier is missing",
                        List.of(),
                        "Missing required field: courier"),
                Arguments.of("courier is blank",
                        List.of(TransactionFixture.stubTransactionMetadata("courier", "")),
                        "Missing required field: courier"),
                Arguments.of("courier does not accept payment method",
                        List.of(TransactionFixture.stubTransactionMetadata("courier", "courier3")),
                        "Courier courier3 does not accept this payment method"));
    }

    @ParameterizedTest
    @MethodSource("arguments")
    @DisplayName("validate transaction with invalid required fields")
    public void validationFailure(String scenario, List<TransactionMetadata> metadata, String expectedError) {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, 1.0);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> transactionProcessor.validateTransactionOrException(transaction, metadata));

        assertEquals(expectedError, exception.getMessage());
    }

    @Test
    @DisplayName("validate transaction: happy flow")
    public void validateTransaction() {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, 1.01);
        var metadata = List.of(TransactionFixture.stubTransactionMetadata("courier", "courier1"));
        assertDoesNotThrow(() -> transactionProcessor.validateTransactionOrException(transaction, metadata));
    }

    @Test
    @DisplayName("process transaction: happy flow")
    public void processTransaction() {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, 1.01);
        transactionProcessor.processTransaction(transaction);

        assertEquals(new BigDecimal("101.00"), transaction.getFinalPrice(), "final price");
        assertEquals(5, transaction.getPoints(), "points");
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.99, 1.03, 1.1})
    @DisplayName("validate transaction: invalid price modifier")
    public void invalidPriceModifier(double priceModifier) {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, priceModifier);
        var metadata = List.of(TransactionFixture.stubTransactionMetadata("courier", "courier1"));
        ValidationException exception = assertThrows(ValidationException.class,
                () -> transactionProcessor.validateTransactionOrException(transaction, metadata));

        assertEquals("Invalid price modifier. Expected range: 1.0 to 1.02", exception.getMessage());
    }
}
