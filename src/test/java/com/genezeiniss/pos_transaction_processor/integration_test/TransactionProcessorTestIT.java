package com.genezeiniss.pos_transaction_processor.integration_test;

import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.fixture.TransactionFixture;
import com.genezeiniss.pos_transaction_processor.repository.TransactionMetadataRepository;
import com.genezeiniss.pos_transaction_processor.repository.TransactionRepository;
import com.genezeiniss.pos_transaction_processor.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class TransactionProcessorTestIT {

    // todo: create command to run integration test on demand

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMetadataRepository transactionMetadataRepository;

    @Test
    @DisplayName("persist transaction with single metadata entry")
    void persistTransactionWithMetadata() {

        var transaction = TransactionFixture.stubTransaction(PaymentMethod.CASH_ON_DELIVERY, 1.0);
        var metadata = TransactionFixture.stubTransactionMetadata("courier", "yamato");
        var metadataList = List.of(metadata);

        transactionService.processTransaction(transaction, metadataList);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var storedTransaction = transactionRepository.findById(transaction.getId());

            assertNotNull(storedTransaction);
            assertEquals(transaction.getPaymentMethod(), storedTransaction.getPaymentMethod(), "payment method");
            assertEquals(transaction.getOriginalPrice(), storedTransaction.getOriginalPrice(), "price");
            assertEquals(transaction.getPriceModifier(), storedTransaction.getPriceModifier(), "price modifier");
            assertEquals(transaction.getFinalPrice(), storedTransaction.getFinalPrice(), "final price");
            assertEquals(transaction.getPoints(), storedTransaction.getPoints(), "points");
        });

        await().atMost(5, SECONDS).untilAsserted(() -> {
            var storedMetadata = transactionMetadataRepository.findByTransactionId(transaction.getId());

            assertEquals(1, storedMetadata.size(), "transaction metadata size");
            assertEquals(metadata.getAttribute(), storedMetadata.get(0).getAttribute(), "transaction metadata attribute");
            assertEquals(metadata.getData(), storedMetadata.get(0).getData(), "transaction metadata data");
        });
    }

    @Test
    @DisplayName("persist transaction without metadata")
    void persistTransactionWithoutMetadata() {
        var transaction = TransactionFixture.stubTransaction(PaymentMethod.CASH, 1.0);
        transactionService.processTransaction(transaction, List.of());

        await().atMost(5, SECONDS).untilAsserted(() -> {
            assertNotNull(transactionRepository.findById(transaction.getId()), "transaction should be persisted");
        });

        assertTrue(transactionMetadataRepository.findByTransactionId(transaction.getId()).isEmpty(), "transaction metadata should not be persisted");
    }

    @Test
    @DisplayName("persist transaction with multiple metadata entries")
    void persistTransactionWithMultipleMetadata() {
        var transaction = TransactionFixture.stubTransaction(PaymentMethod.BANK_TRANSFER, 1.0);
        var bankMetadata = TransactionFixture.stubTransactionMetadata("bank", "Default Bank");
        var accountMetadata = TransactionFixture.stubTransactionMetadata("accountNumber", "1234567890");
        var metadataList = List.of(bankMetadata, accountMetadata);

        transactionService.processTransaction(transaction, metadataList);

        await().atMost(5, SECONDS).untilAsserted(() -> {
            assertNotNull(transactionRepository.findById(transaction.getId()));
        });
        await().atMost(5, SECONDS).untilAsserted(() -> {
            var storedMetadata = transactionMetadataRepository.findByTransactionId(transaction.getId());
            assertEquals(2, storedMetadata.size(), "metadata entries size should be 2");
            assertTrue(storedMetadata.stream()
                    .map(TransactionMetadata::getAttribute)
                    .toList()
                    .containsAll(List.of("bank", "accountNumber")), "metadata attributes");
        });
    }
}
