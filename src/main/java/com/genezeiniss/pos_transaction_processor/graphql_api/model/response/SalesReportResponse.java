package com.genezeiniss.pos_transaction_processor.graphql_api.model.response;

import lombok.Data;

import java.util.List;

@Data
public class SalesReportResponse {

    private List<SalesReport> sales;
}
