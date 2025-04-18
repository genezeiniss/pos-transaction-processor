package com.genezeiniss.pos_transaction_processor.domain;

import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class Transaction extends BaseEntity {

    protected String userId;
    protected String customerId;
    protected BigDecimal price;
    protected BigDecimal priceModifier;
    protected PaymentMethod paymentMethod;
    protected Instant datetime;
    private BigDecimal finalPrice;
    private int points;
}
