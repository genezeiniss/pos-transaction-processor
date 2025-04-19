package com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers;

import com.genezeiniss.pos_transaction_processor.domain.PriceModifierRange;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PaymentMethodModifier {

    private double pointsMultiplier;
    private PriceModifierRange priceModifierRange;
}
