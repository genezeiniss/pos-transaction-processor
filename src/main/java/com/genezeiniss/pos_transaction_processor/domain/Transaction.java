package com.genezeiniss.pos_transaction_processor.domain;

import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class Transaction extends BaseEntity {

    protected String userId;
    protected String customerId;
    protected BigDecimal price;
    protected double priceModifier;
    protected PaymentMethod paymentMethod;
    protected LocalDateTime createdAt;
    private BigDecimal finalPrice;
    private int points;
}
