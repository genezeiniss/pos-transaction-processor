package com.genezeiniss.pos_transaction_processor.configuration;

import com.genezeiniss.pos_transaction_processor.domain.PriceModifierRange;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PaymentMethodProperties {

    private double pointsMultiplier;
    private PriceModifierRange priceModifierRange;
}
