package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.payment_method_properties.VisaProperties;
import com.genezeiniss.pos_transaction_processor.domain.PriceModifierRange;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.exception.ValidationException;
import com.genezeiniss.pos_transaction_processor.fixture.TransactionFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class VisaTrxProcessorTest {

    private static VisaTrxProcessor transactionProcessor;
    private final PaymentMethod paymentMethod = PaymentMethod.VISA;

    @BeforeAll
    static void setup() {
        VisaProperties properties = new VisaProperties();
        properties.setPointsMultiplier(new BigDecimal("0.03"));
        properties.setPriceModifierRange(new PriceModifierRange(new BigDecimal("0.95"), new BigDecimal("1.0")));

        transactionProcessor = new VisaTrxProcessor(properties);
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("additional info is missing",
                        null,
                        "Missing required field: last4"),
                Arguments.of("last4 is missing",
                        List.of(),
                        "Missing required field: last4"),
                Arguments.of("last4 value is alphanumeric",
                        List.of(TransactionFixture.stubTransactionMetadata("last4", "ab12")),
                        "Invalid last4 value"),
                Arguments.of("last4 value is too short",
                        List.of(TransactionFixture.stubTransactionMetadata("last4", "123")),
                        "Invalid last4 value"),
                Arguments.of("last4 value is too long",
                        List.of(TransactionFixture.stubTransactionMetadata("last4", "12345")),
                        "Invalid last4 value"));
    }

    @ParameterizedTest
    @MethodSource("arguments")
    @DisplayName("validate transaction with invalid required fields and invalid price modifier")
    public void validationFailure(String scenario, List<TransactionMetadata> metadata, String expectedError) {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, new BigDecimal("0.94"));
        ValidationException exception = assertThrows(ValidationException.class,
                () -> transactionProcessor.validateTransactionOrException(transaction, metadata));

        assertEquals(String.format("%s; Invalid price modifier. Expected range: 0.95 to 1.0", expectedError), exception.getMessage());
    }

    @Test
    @DisplayName("validate transaction: happy flow")
    public void validateTransaction() {
        var transaction = TransactionFixture.stubTransaction(paymentMethod, new BigDecimal("1.0"));
        var metadata = List.of(TransactionFixture.stubTransactionMetadata("last4", "1234"));
        assertDoesNotThrow(() -> transactionProcessor.validateTransactionOrException(transaction, metadata));

    }
}
