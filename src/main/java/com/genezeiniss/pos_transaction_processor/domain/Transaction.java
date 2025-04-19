package com.genezeiniss.pos_transaction_processor.domain;

import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class Transaction extends BaseEntity {

    protected String userId;
    protected String customerId;
    protected PaymentMethod paymentMethod;
    protected BigDecimal originalPrice;
    protected double priceModifier;
    private BigDecimal finalPrice;
    private int points;
    protected OffsetDateTime createdAt;
}
