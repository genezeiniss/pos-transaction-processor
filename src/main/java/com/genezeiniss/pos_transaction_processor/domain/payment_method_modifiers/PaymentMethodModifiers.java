package com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("payment-method-modifiers")
public class PaymentMethodModifiers {

    private AmexModifier amex;
    private BankTransferModifier bankTransfer;
    private CashModifier cash;
    private CashOnDeliveryModifier cashOnDelivery;
    private ChequeModifier cheque;
    private GrabPayModifier grabPay;
    private JcbModifier jcb;
    private LinePayModifier linePay;
    private MastercardModifier mastercard;
    private PaypayModifier paypay;
    private PointsModifier points;
    private VisaModifier visa;
}
