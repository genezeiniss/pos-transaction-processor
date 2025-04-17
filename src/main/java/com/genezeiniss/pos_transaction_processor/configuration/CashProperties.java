package com.genezeiniss.pos_transaction_processor.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "payment-method-modifiers.cash")
public class CashProperties extends PaymentMethodProperties {
}
