package com.genezeiniss.pos_transaction_processor.integration_test;

import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.fixture.SaleFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureGraphQlTester
class SaleControllerTestIT {

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    @DisplayName("process sale with cash payment method")
    void processSaleWithCash() {
        var mutation = """
                mutation ProcessSale($request: SaleRequest!) {
                    processSale(request: $request) {
                        finalPrice
                        points
                    }
                }
                """;
        var response = graphQlTester.document(mutation)
                .variable("request", Map.of(
                        "customerId", SaleFixture.CUSTOMER_ID,
                        "price", SaleFixture.PRICE,
                        "priceModifier", SaleFixture.PRICE_MODIFIER,
                        "paymentMethod", PaymentMethod.CASH.name(),
                        "datetime", SaleFixture.DATETIME
                ))
                .execute();

        response
                .path("data.processSale.finalPrice").entity(String.class).isEqualTo("90.00")
                .path("data.processSale.points").entity(Integer.class).isEqualTo(5);
    }

    @Test
    @DisplayName("process sale with bank transfer payment method")
    void processSaleWithBankTransfer() {
        var mutation = """
                mutation ProcessSale($request: SaleRequest!) {
                    processSale(request: $request) {
                        finalPrice
                        points
                    }
                }
                """;

        var response = graphQlTester.document(mutation)
                .variable("request", Map.of(
                        "customerId", SaleFixture.CUSTOMER_ID,
                        "price", SaleFixture.PRICE,
                        "priceModifier", 1.0,
                        "paymentMethod", PaymentMethod.BANK_TRANSFER.name(),
                        "datetime", SaleFixture.DATETIME,
                        "additionalItem", Map.of(
                                "bank", "Bank of America",
                                "accountNumber", "GB29NWBK60161331926819"
                        )
                ))
                .execute();

        response
                .path("data.processSale.finalPrice").entity(String.class).isEqualTo("100.00")
                .path("data.processSale.points").entity(Integer.class).isEqualTo(0);
    }

    @Test
    @DisplayName("process sale with invalid payment method")
    void processSaleWithInvalidPaymentMethod() {
        var mutation = """
                mutation ProcessSale($request: SaleRequest!) {
                    processSale(request: $request) {
                        finalPrice
                        points
                    }
                }
                """;

        graphQlTester.document(mutation)
                .variable("request", Map.of(
                        "customerId", SaleFixture.CUSTOMER_ID,
                        "price", SaleFixture.PRICE,
                        "priceModifier", SaleFixture.PRICE_MODIFIER,
                        "paymentMethod", "INVALID_PAYMENT_METHOD",
                        "datetime", SaleFixture.DATETIME
                ))
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertEquals(1, errors.size(), "should have one error");
                    assertEquals("Unsupported payment methods: INVALID_PAYMENT_METHOD",
                            errors.get(0).getExtensions().get("error"), "error message");
                });
    }

    @Test
    @DisplayName("process sale with invalid price")
    void processSaleWithInvalidPrice() {
        var mutation = """
                mutation ProcessSale($request: SaleRequest!) {
                    processSale(request: $request) {
                        finalPrice
                        points
                    }
                }
                """;

        graphQlTester.document(mutation)
                .variable("request", Map.of(
                        "customerId", SaleFixture.CUSTOMER_ID,
                        "price", "invalid-price",
                        "priceModifier", SaleFixture.PRICE_MODIFIER,
                        "paymentMethod", PaymentMethod.CASH.name(),
                        "datetime", SaleFixture.DATETIME
                ))
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertEquals(1, errors.size(), "should have one error");
                    assertNotNull(errors.get(0).getMessage(), "error message should not be null");
                });
    }

    @Test
    @DisplayName("process sale with missing required fields")
    void processSaleWithMissingRequiredFields() {
        var mutation = """
                mutation ProcessSale($request: SaleRequest!) {
                    processSale(request: $request) {
                        finalPrice
                        points
                    }
                }
                """;

        graphQlTester.document(mutation)
                .variable("request", Map.of(
                        "customerId", SaleFixture.CUSTOMER_ID,
                        "price", SaleFixture.PRICE,
                        "priceModifier", SaleFixture.PRICE_MODIFIER,
                        "paymentMethod", PaymentMethod.BANK_TRANSFER.name(),
                        "datetime", SaleFixture.DATETIME
                ))
                .execute()
                .errors()
                .satisfy(errors -> {
                    assertEquals(1, errors.size(), "should have one error");
                    assertNotNull(errors.get(0).getMessage(), "error message should not be null");
                });
    }
} 