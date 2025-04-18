package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.BankTransferProperties;
import com.genezeiniss.pos_transaction_processor.domain.PriceModifierRange;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BankTransferTrxProcessorTest {

    private static BankTransferTrxProcessor transactionProcessor;
    private final PaymentMethod paymentMethod = PaymentMethod.BANK_TRANSFER;

    @BeforeAll
    static void setup() {
        BankTransferProperties properties = new BankTransferProperties();
        properties.setPointsMultiplier(new BigDecimal("0"));
        properties.setPriceModifierRange(new PriceModifierRange(new BigDecimal("1.0"), new BigDecimal("1.0")));

        transactionProcessor = new BankTransferTrxProcessor(properties);
    }

    private static Stream<Arguments> arguments() {
        return Stream.of(
                Arguments.of("additional info is missing",
                        null,
                        List.of("Missing required field: bank", "Missing required field: accountNumber")),
                Arguments.of("bank is missing",
                        List.of(new TransactionMetadata("accountNumber", "123456")),
                        List.of("Missing required field: bank")),
                Arguments.of("account number is missing",
                        List.of(new TransactionMetadata("bank", "Bank of America")),
                        List.of("Missing required field: accountNumber")),
                Arguments.of("invalid iban",
                        List.of(new TransactionMetadata("bank", "Bank of America"),
                                new TransactionMetadata("accountNumber", "12AB3456789012")),
                        List.of("Invalid accountNumber value")),
                Arguments.of("invalid bank and blank account number",
                        List.of(new TransactionMetadata("bank", "Chase&Co"),
                                new TransactionMetadata("accountNumber", " ")),
                        List.of("Invalid bank value", "Missing required field: accountNumber")),
                Arguments.of("empty bank and invalid account number",
                        List.of(new TransactionMetadata("bank", ""),
                                new TransactionMetadata("accountNumber", "ABC123456")),
                        List.of("Missing required field: bank", "Invalid accountNumber value")));
    }

    @ParameterizedTest
    @MethodSource("arguments")
    @DisplayName("validate transaction with invalid required fields")
    public void validationFailure(String scenario, List<TransactionMetadata> metadata, List<String> expectedErrors) {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, new BigDecimal("1.0"), metadata);
        List<String> errors = transactionProcessor.validateTransaction(transaction);

        assertEquals(expectedErrors.size(), errors.size(), "number of errors");
        assertTrue(errors.containsAll(expectedErrors), "error messages");
    }

    @Test
    @DisplayName("validate transaction: happy flow")
    public void validateTransaction() {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, new BigDecimal("1.0"),
                List.of(new TransactionMetadata("bank", "Bank of America"),
                        new TransactionMetadata("accountNumber", "GB29NWBK60161331926819")));
        List<String> errors = transactionProcessor.validateTransaction(transaction);
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("process transaction: happy flow")
    public void processTransaction() {

        var transaction = TransactionFixture.stubTransaction(paymentMethod, new BigDecimal("1.0"),
                List.of(new TransactionMetadata("bank", "Bank of America"),
                        new TransactionMetadata("accountNumber", "GB29NWBK60161331926819")));
        transactionProcessor.processTransaction(transaction);

        assertEquals(new BigDecimal("100.00"), transaction.getFinalPrice(), "final price");
        assertEquals(0, transaction.getPoints(), "points");
    }
}
