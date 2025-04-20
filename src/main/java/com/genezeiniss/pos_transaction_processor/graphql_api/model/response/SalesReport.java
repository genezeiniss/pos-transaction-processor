package com.genezeiniss.pos_transaction_processor.graphql_api.model.response;

import lombok.Data;

@Data
public class SalesReport {

    private String datetime;
    private String sales;
    private int points;
}
