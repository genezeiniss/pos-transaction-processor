package com.genezeiniss.pos_transaction_processor.domain;

import java.math.BigDecimal;

public record PriceModifierRange(BigDecimal min, BigDecimal max) {
}
