package com.genezeiniss.pos_transaction_processor.configuration;

import com.genezeiniss.pos_transaction_processor.graphql_api.model.request.SaleRequest;

import java.time.format.DateTimeFormatter;

public class SaleRequestResolver {

    private static final DateTimeFormatter ISO_FORMAT_WITH_SECONDS =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX");

    public String getDatetime(SaleRequest saleRequest) {
        return saleRequest.getDatetime().format(ISO_FORMAT_WITH_SECONDS);
    }
}
