package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.domain.PriceModifierRange;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers.ChequeModifier;
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

public class ChequeTrxProcessorTest {

    private static ChequeTrxProcessor transactionProcessor;
    private final PaymentMethod paymentMethod = PaymentMethod.CHEQUE;

    @BeforeAll
    static void setup() {
        ChequeModifier properties = new ChequeModifier();
        properties.setPointsMultiplier(0);
        properties.setPriceModifierRange(new PriceModifierRange(0.9, 1.0));

        transactionProcessor = new ChequeTrxProcessor(properties);
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("additional info is missing",
                        null,
                        List.of("Missing required field: bank", "Missing required field: chequeNumber")),
                Arguments.of("bank is missing",
                        List.of(TransactionFixture.stubTransactionMetadata("chequeNumber", "123456789012")),
                        List.of("Missing required field: bank")),
                Arguments.of("cheque number is missing",
                        List.of(TransactionFixture.stubTransactionMetadata("bank", "Bank of America")),
                        List.of("Missing required field: chequeNumber")),
                Arguments.of("cheque number too short",
                        List.of(TransactionFixture.stubTransactionMetadata("bank", "Bank of America"),
                                TransactionFixture.stubTransactionMetadata("chequeNumber", "12345")),
                        List.of("Invalid chequeNumber value")),
                Arguments.of("empty bank and cheque number contains letters",
                        List.of(TransactionFixture.stubTransactionMetadata("bank", ""),
                                TransactionFixture.stubTransactionMetadata("chequeNumber", "ABC123456")),
                        List.of("Missing required field: bank", "Invalid chequeNumber value")));
    }

    @ParameterizedTest
    @MethodSource("arguments")
    @DisplayName("validate transaction with invalid required fields")
    public void validationFailure(String scenario, List<TransactionMetadata> metadata, List<String> expectedErrors) {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, 1.0);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> transactionProcessor.validateTransactionOrException(transaction, metadata));

        assertEquals(String.join("; ", expectedErrors), exception.getMessage());
    }

    @Test
    @DisplayName("validate transaction: happy flow")
    public void validateTransaction() {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, 1.0);
        var metadata = List.of(
                        TransactionFixture.stubTransactionMetadata("bank", "Bank of America"),
                TransactionFixture.stubTransactionMetadata("chequeNumber", "123456789012"));
        assertDoesNotThrow(() -> transactionProcessor.validateTransactionOrException(transaction, metadata));
    }

    @Test
    @DisplayName("process transaction: happy flow")
    public void processTransaction() {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, 0.99);
        transactionProcessor.processTransaction(transaction);

        assertEquals(new BigDecimal("99.00"), transaction.getFinalPrice(), "final price");
        assertEquals(0, transaction.getPoints(), "points");
    }
}
