package com.genezeiniss.pos_transaction_processor.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TransactionMetadata extends BaseEntity {

    private String transactionId;
    private String attribute;
    // todo: this field can contain sensitive data, such as bank account number or cheque number.
    //  Consider implementing encryption for sensitive transaction data
    private String data;
}
