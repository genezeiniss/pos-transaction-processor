package com.genezeiniss.pos_transaction_processor.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "payment-method-modifiers.line-pay")
public class LinePayProperties extends PaymentMethodProperties {
}
