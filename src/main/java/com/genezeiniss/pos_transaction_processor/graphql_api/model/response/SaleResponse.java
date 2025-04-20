package com.genezeiniss.pos_transaction_processor.graphql_api.model.response;

import lombok.Data;

@Data
public class SaleResponse {

    private String finalPrice;
    private int points;
}
