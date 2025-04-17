package com.genezeiniss.pos_transaction_processor.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "payment-method-modifiers.jcb")
public class JcbProperties extends PaymentMethodProperties {
}
