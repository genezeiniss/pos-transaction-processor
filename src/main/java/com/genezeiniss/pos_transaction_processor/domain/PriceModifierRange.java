package com.genezeiniss.pos_transaction_processor.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PriceModifierRange {
    private double min;
    private double max;
}
