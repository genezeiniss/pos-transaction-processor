package com.genezeiniss.pos_transaction_processor.configuration.payment_method_properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "payment-method-modifiers.cheque")
public class ChequeProperties extends PaymentMethodProperties {
}
