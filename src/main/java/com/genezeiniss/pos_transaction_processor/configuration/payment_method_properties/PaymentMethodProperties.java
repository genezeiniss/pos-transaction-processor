package com.genezeiniss.pos_transaction_processor.configuration.payment_method_properties;

import com.genezeiniss.pos_transaction_processor.domain.PriceModifierRange;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public abstract class PaymentMethodProperties {

    private BigDecimal pointsMultiplier;
    private PriceModifierRange priceModifierRange;
}
