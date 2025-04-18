package com.genezeiniss.pos_transaction_processor.domain;

import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class Transaction {

    protected String userId;
    protected String customerId;
    protected BigDecimal price;
    protected BigDecimal priceModifier;
    protected PaymentMethod paymentMethod;
    protected Instant datetime;
    protected List<TransactionMetadata> metadata;
    private BigDecimal finalPrice;
    private int points;
}
