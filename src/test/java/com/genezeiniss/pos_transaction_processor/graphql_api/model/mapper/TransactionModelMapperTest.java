package com.genezeiniss.pos_transaction_processor.graphql_api.model.mapper;

import com.genezeiniss.pos_transaction_processor.configuration.TransactionProcessorApplicationConfig;
import com.genezeiniss.pos_transaction_processor.domain.TransactionMetadata;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.fixture.SaleFixture;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.MappingException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionModelMapperTest {

    private static TransactionProcessorModelMapper transactionProcessorModelMapper;

    @BeforeAll
    static void setup() {
        transactionProcessorModelMapper = new TransactionProcessorModelMapper(new TransactionProcessorApplicationConfig().modelMapper());
        transactionProcessorModelMapper.init();
    }

    @Test
    @DisplayName("map sale request to transaction")
    void mapSaleRequestToTransaction() {

        var saleRequest = SaleFixture.stubSaleRequest();
        var transaction = transactionProcessorModelMapper.mapSaleRequestToTransaction(saleRequest);

        assertNotNull(transaction, "transaction not null");
        assertEquals(SaleFixture.CUSTOMER_ID, transaction.getCustomerId(), "customer id");
        assertEquals(new BigDecimal(SaleFixture.PRICE), transaction.getOriginalPrice(), "original price");
        assertEquals(SaleFixture.PRICE_MODIFIER, transaction.getPriceModifier(), "price modifier");
        assertEquals(PaymentMethod.valueOf(SaleFixture.PAYMENT_METHOD), transaction.getPaymentMethod(), "payment method");
        assertEquals(OffsetDateTime.parse(SaleFixture.DATETIME), transaction.getCreatedAt(), "datetime");
    }

    @Test
    @DisplayName("map sale request to transaction: invalid price")
    void mapSaleRequestWithInvalidPriceToTransaction() {

        var saleRequest = SaleFixture.stubSaleRequest();
        saleRequest.setPrice("invalid-price");

        assertThrows(MappingException.class, () -> {
            transactionProcessorModelMapper.mapSaleRequestToTransaction(saleRequest);
        });
    }

    @Test
    @DisplayName("map sale request to transaction: unknown payment method")
    void mapSaleRequestWithUnknownPaymentMethodToTransaction() {

        var saleRequest = SaleFixture.stubSaleRequest();
        saleRequest.setPaymentMethod("CREDIT_CARD");

        var exception = assertThrows(MappingException.class, () -> {
            transactionProcessorModelMapper.mapSaleRequestToTransaction(saleRequest);
        });

        assertEquals("Unsupported payment methods: CREDIT_CARD", exception.getCause().getMessage(), "exception message");
    }

    @Test
    @DisplayName("map additional item to transaction metadata")
    void mapToTransactionMetadata() {

        var additionalItem = Map.of(
                "item1", "value1",
                "item2", "value2");
        var transactionMetadata = transactionProcessorModelMapper.mapToTransactionMetadata(additionalItem);

        assertNotNull(transactionMetadata, "transaction metadata not null");
        assertEquals(2, transactionMetadata.size());
        assertTrue(transactionMetadata.stream().map(TransactionMetadata::getAttribute).toList().containsAll(List.of("item1", "item2")), "attributes");
        assertTrue(transactionMetadata.stream().map(TransactionMetadata::getData).toList().containsAll(List.of("value1", "value2")), "data");
    }

    @Test
    @DisplayName("map null to transaction metadata")
    void mapNullToTransactionMetadata() {

        var transactionMetadata = transactionProcessorModelMapper.mapToTransactionMetadata(null);

        assertNotNull(transactionMetadata, "transaction metadata not null");
        assertTrue(transactionMetadata.isEmpty());
    }
}