package com.genezeiniss.pos_transaction_processor.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseEntity {

    private String id;
    private LocalDateTime recordCreatedAt;
}
