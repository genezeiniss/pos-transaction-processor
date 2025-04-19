package com.genezeiniss.pos_transaction_processor.graphql_api.model.request;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class SalesReportRequest {

    private OffsetDateTime startDateTime;
    private OffsetDateTime endDateTime;
}
