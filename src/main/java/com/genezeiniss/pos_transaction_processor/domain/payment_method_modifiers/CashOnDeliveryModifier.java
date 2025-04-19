package com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Component
public class CashOnDeliveryModifier extends PaymentMethodModifier {

    private List<String> allowedCouriers;
}
