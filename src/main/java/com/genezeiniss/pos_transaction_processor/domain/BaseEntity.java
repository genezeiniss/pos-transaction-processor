package com.genezeiniss.pos_transaction_processor.domain;

import lombok.Data;

import java.time.Instant;

@Data
public class BaseEntity {

    private String id;
    private Instant recordCreatedAt;
}
