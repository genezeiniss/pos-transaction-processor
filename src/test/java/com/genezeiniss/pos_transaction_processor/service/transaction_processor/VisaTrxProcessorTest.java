package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.VisaProperties;
import com.genezeiniss.pos_transaction_processor.domain.PriceModifierRange;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.fixture.TransactionFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VisaTrxProcessorTest {

    private static VisaTrxProcessor transactionProcessor;
    private final PaymentMethod paymentMethod = PaymentMethod.VISA;

    @BeforeAll
    static void setup() {
        VisaProperties properties = new VisaProperties();
        properties.setPointsMultiplier(0.03);
        properties.setPriceModifierRange(new PriceModifierRange(0.95, 1.0));

        transactionProcessor = new VisaTrxProcessor(properties);
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("additional info is missing",
                        null,
                        "Missing required field: last4"),
                Arguments.of("last4 is missing",
                        Map.of(),
                        "Missing required field: last4"),
                Arguments.of("last4 value is alphanumeric",
                        Map.of("last4", "ab12"),
                        "Invalid last4 value"),
                Arguments.of("last4 value is too short",
                        Map.of("last4", "123"),
                        "Invalid last4 value"),
                Arguments.of("last4 value is too long",
                        Map.of("last4", "12345"),
                        "Invalid last4 value"));
    }

    @ParameterizedTest
    @MethodSource("arguments")
    @DisplayName("validate transaction with invalid required fields and invalid price modifier")
    public void validationFailure(String scenario, Map<String, String> additionalInfo, String expectedError) {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, 0.94, additionalInfo);
        List<String> errors = transactionProcessor.validateTransaction(transaction);

        assertEquals(2, errors.size(), "number of errors");
        assertTrue(errors.containsAll(List.of(expectedError, "Invalid price modifier. Expected range: 0.95 to 1.0")), "error messages");
    }

    @Test
    @DisplayName("validate transaction: happy flow")
    public void validateTransaction() {
        var transaction = TransactionFixture.stubTransaction(paymentMethod, 1.0, Map.of("last4", "1234"));
        List<String> errors = transactionProcessor.validateTransaction(transaction);
        assertTrue(errors.isEmpty());
    }
}
