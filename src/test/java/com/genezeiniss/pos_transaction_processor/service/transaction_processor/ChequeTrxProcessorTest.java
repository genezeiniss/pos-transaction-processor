package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.ChequeProperties;
import com.genezeiniss.pos_transaction_processor.domain.PriceModifierRange;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.fixture.TransactionFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChequeTrxProcessorTest {

    private static ChequeTrxProcessor transactionProcessor;
    private final PaymentMethod paymentMethod = PaymentMethod.CHEQUE;

    @BeforeAll
    static void setup() {
        ChequeProperties properties = new ChequeProperties();
        properties.setPointsMultiplier(new BigDecimal("0"));
        properties.setPriceModifierRange(new PriceModifierRange(new BigDecimal("0.9"), new BigDecimal("1.0")));

        transactionProcessor = new ChequeTrxProcessor(properties);
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("additional info is missing",
                        null,
                        List.of("Missing required field: bank", "Missing required field: chequeNumber")),
                Arguments.of("bank is missing",
                        Map.of("chequeNumber", "123456789012"),
                        List.of("Missing required field: bank")),
                Arguments.of("cheque number is missing",
                        Map.of("bank", "Bank of America"),
                        List.of("Missing required field: chequeNumber")),
                Arguments.of("cheque number too short",
                        Map.of("bank", "Bank of America", "chequeNumber", "12345"),
                        List.of("Invalid chequeNumber value")),
                Arguments.of("empty bank and cheque number contains letters",
                        Map.of("bank", "", "chequeNumber", "ABC123456"),
                        List.of("Missing required field: bank", "Invalid chequeNumber value")));
    }

    @ParameterizedTest
    @MethodSource("arguments")
    @DisplayName("validate transaction with invalid required fields")
    public void validationFailure(String scenario, Map<String, String> additionalInfo, List<String> expectedErrors) {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, new BigDecimal("1.0"), additionalInfo);
        List<String> errors = transactionProcessor.validateTransaction(transaction);

        assertEquals(expectedErrors.size(), errors.size(), "number of errors");
        assertTrue(errors.containsAll(expectedErrors), "error messages");
    }

    @Test
    @DisplayName("validate transaction: happy flow")
    public void validateTransaction() {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, new BigDecimal("1.0"), Map.of("bank", "Bank of America", "chequeNumber", "123456789012"));
        List<String> errors = transactionProcessor.validateTransaction(transaction);
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("process transaction: happy flow")
    public void processTransaction() {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, new BigDecimal("0.99"), Map.of("bank", "Bank of America", "chequeNumber", "123456789012"));
        transactionProcessor.processTransaction(transaction);

        assertEquals(new BigDecimal("99.00"), transaction.getFinalPrice(), "final price");
        assertEquals(0, transaction.getPoints(), "points");
    }
}
