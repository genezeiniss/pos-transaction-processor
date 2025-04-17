package com.genezeiniss.pos_transaction_processor.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "payment-method-modifiers.cash-on-delivery")
public class CashOnDeliveryProperties extends PaymentMethodProperties {

    private List<String> allowedCouriers;
}
