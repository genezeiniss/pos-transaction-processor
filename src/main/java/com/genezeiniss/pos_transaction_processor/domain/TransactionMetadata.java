package com.genezeiniss.pos_transaction_processor.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransactionMetadata extends BaseEntity {

    private String transactionId;
    private String attribute;
    private String data;
}
