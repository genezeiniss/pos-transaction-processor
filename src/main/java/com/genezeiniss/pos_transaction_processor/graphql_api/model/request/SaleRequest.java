package com.genezeiniss.pos_transaction_processor.graphql_api.model.request;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
public class SaleRequest {

    private String customerId;
    private String price;
    private double priceModifier;
    private String paymentMethod;
    private OffsetDateTime datetime;
    private Map<String, String> additionalItem;
}
