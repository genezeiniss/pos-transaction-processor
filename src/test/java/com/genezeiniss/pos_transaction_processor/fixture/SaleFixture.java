package com.genezeiniss.pos_transaction_processor.fixture;

import com.genezeiniss.pos_transaction_processor.graphql_api.model.request.SaleRequest;

import java.time.OffsetDateTime;

public class SaleFixture {

    public static String CUSTOMER_ID = "customer-id";
    public static String PRICE = "100.00";
    public static double PRICE_MODIFIER = 0.9;
    public static String PAYMENT_METHOD = "CASH";
    public static String DATETIME = "2022-09-01T00:00:00Z";

    public static SaleRequest stubSaleRequest() {
        var saleRequest = new SaleRequest();
        saleRequest.setCustomerId(CUSTOMER_ID);
        saleRequest.setPrice(PRICE);
        saleRequest.setPriceModifier(PRICE_MODIFIER);
        saleRequest.setPaymentMethod(PAYMENT_METHOD);
        saleRequest.setDatetime(OffsetDateTime.parse(DATETIME));
        return saleRequest;
    }
}
