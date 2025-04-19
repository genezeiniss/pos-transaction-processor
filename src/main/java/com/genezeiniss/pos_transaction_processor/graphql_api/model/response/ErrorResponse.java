package com.genezeiniss.pos_transaction_processor.graphql_api.model.response;

import lombok.Data;

@Data
public class ErrorResponse {

    private String error;

    public ErrorResponse(String errorMessage) {
        this.error = errorMessage;
    }
}
