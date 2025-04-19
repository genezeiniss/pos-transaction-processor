//package com.genezeiniss.pos_transaction_processor.graphql_api.model.mapper;
//
//import com.genezeiniss.pos_transaction_processor.configuration.TransactionProcessorApplicationConfig;
//import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
//import com.genezeiniss.pos_transaction_processor.graphql_api.model.request.TransactionRequest;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//public class TransactionModelMapperTest {
//
//    private static TransactionModelMapper transactionModelMapper;
//    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
//
//    @BeforeAll
//    static void setup() {
//        transactionModelMapper = new TransactionModelMapper(new TransactionProcessorApplicationConfig().modelMapper());
//    }
//
//
//    @Test
//    void mapTransactionRequestToTransaction_ShouldMapFieldsCorrectly() {
//
//        var transactionRequest = new TransactionRequest();
//        transactionRequest.setCustomerId("custId");
//        transactionRequest.setPrice("100.50");
//        transactionRequest.setPriceModifier(0.9);
//        transactionRequest.setPaymentMethod("CASH");
//        transactionRequest.setDatetime("2023-01-01T10:15:30");
//
//        var transaction = transactionModelMapper.mapTransactionRequestToTransaction(transactionRequest);
//
//        // Then
//        assertNotNull(transaction);
//        assertEquals("cust-123", transaction.getCustomerId());
//        assertEquals(new BigDecimal("100.50"), transaction.getOriginalPrice());
//        assertEquals(0.9, transaction.getPriceModifier(), 0.001);
//        assertEquals(PaymentMethod.CASH, transaction.getPaymentMethod());
//        assertEquals(LocalDateTime.parse("2023-01-01T10:15:30", formatter), transaction.getCreatedAt());
//    }
/// /
/// /    @Test
/// /    void mapTransactionRequestToTransaction_ShouldHandleNullValues() {
/// /        // Given
/// /        TransactionRequest request = new TransactionRequest();
/// /        request.setPrice("100.50");
/// /        request.setDatetime("2023-01-01T10:15:30");
/// /
/// /        // When
/// /        Transaction result = transactionModelMapper.mapTransactionRequestToTransaction(request);
/// /
/// /        // Then
/// /        assertNotNull(result);
/// /        assertNull(result.getCustomerId());
/// /        assertEquals(new BigDecimal("100.50"), result.getOriginalPrice());
/// /        assertEquals(0.0, result.getPriceModifier(), 0.001);
/// /        assertNull(result.getPaymentMethod());
/// /        assertEquals(LocalDateTime.parse("2023-01-01T10:15:30", formatter), result.getCreatedAt());
/// /    }
/// /
/// /    @Test
/// /    void mapToTransactionMetadata_ShouldConvertMapToMetadataList() {
/// /        // Given
/// /        Map<String, String> additionalItems = Map.of(
/// /                "color", "red",
/// /                "size", "large"
/// /        );
/// /
/// /        // When
/// /        List<TransactionMetadata> result = transactionModelMapper.mapToTransactionMetadata(additionalItems);
/// /
/// /        // Then
/// /        assertEquals(2, result.size());
/// /        assertTrue(result.stream().anyMatch(m -> "color".equals(m.getAttribute()) && "red".equals(m.getData())));
/// /        assertTrue(result.stream().anyMatch(m -> "size".equals(m.getAttribute()) && "large".equals(m.getData())));
/// /    }
/// /
/// /    @Test
/// /    void mapToTransactionMetadata_WithEmptyMap_ShouldReturnEmptyList() {
/// /        // Given
/// /        Map<String, String> additionalItems = Map.of();
/// /
/// /        // When
/// /        List<TransactionMetadata> result = transactionModelMapper.mapToTransactionMetadata(additionalItems);
/// /
/// /        // Then
/// /        assertTrue(result.isEmpty());
/// /    }
/// /
/// /    @Test
/// /    void mapToTransactionMetadata_WithNullInput_ShouldReturnEmptyList() {
/// /        // When
/// /        List<TransactionMetadata> result = transactionModelMapper.mapToTransactionMetadata(null);
/// /
/// /        // Then
/// /        assertTrue(result.isEmpty());
/// /    }
/// /
/// /    @Test
/// /    void mapTransactionRequestToTransaction_ShouldHandleInvalidPrice() {
/// /        // Given
/// /        TransactionRequest request = new TransactionRequest();
/// /        request.setPrice("invalid");
/// /        request.setDatetime("2023-01-01T10:15:30");
/// /
/// /        // When/Then
/// /        assertThrows(NumberFormatException.class, () -> {
/// /            transactionModelMapper.mapTransactionRequestToTransaction(request);
/// /        });
/// /    }
/// /
/// /    @Test
/// /    void mapTransactionRequestToTransaction_ShouldHandleInvalidDatetime() {
/// /        // Given
/// /        TransactionRequest request = new TransactionRequest();
/// /        request.setPrice("100.00");
/// /        request.setDatetime("invalid-datetime");
/// /
/// /        // When/Then
/// /        assertThrows(Exception.class, () -> {
/// /            transactionModelMapper.mapTransactionRequestToTransaction(request);
/// /        });
/// /    }
/// /}
//}
