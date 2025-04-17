package com.genezeiniss.pos_transaction_processor.domain;

import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class Transaction {

    protected String userId;
    protected String customerId;
    protected double price;
    protected double priceModifier;
    protected PaymentMethod paymentMethod;
    protected Instant datetime;
    protected Map<String, String> additionalInformation;
    //todo: make sure that Graphql return values with two decimals
    private double finalPrice;
    private int points;

}
