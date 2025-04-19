package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers.PaymentMethodModifiers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionProcessorFactory {

    private final PaymentMethodModifiers paymentMethodModifiers;

    public TransactionProcessor getTransactionProcessor(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case PaymentMethod.CASH -> new CashTrxProcessor(paymentMethodModifiers.getCash());
            case PaymentMethod.CASH_ON_DELIVERY ->
                    new CashOnDeliveryTrxProcessor(paymentMethodModifiers.getCashOnDelivery());
            case PaymentMethod.VISA -> new VisaTrxProcessor(paymentMethodModifiers.getVisa());
            case PaymentMethod.MASTERCARD -> new MastercardTrxProcessor(paymentMethodModifiers.getMastercard());
            case PaymentMethod.AMEX -> new AmexTrxProcessor(paymentMethodModifiers.getAmex());
            case PaymentMethod.JCB -> new JcbTrxProcessor(paymentMethodModifiers.getJcb());
            case PaymentMethod.LINE_PAY -> new LinePayTrxProcessor(paymentMethodModifiers.getLinePay());
            case PaymentMethod.PAYPAY -> new PaypayTrxProcessor(paymentMethodModifiers.getPaypay());
            case PaymentMethod.POINTS -> new PointsTrxProcessor(paymentMethodModifiers.getPoints());
            case PaymentMethod.GRAB_PAY -> new GrabPayTrxProcessor(paymentMethodModifiers.getGrabPay());
            case PaymentMethod.BANK_TRANSFER -> new BankTransferTrxProcessor(paymentMethodModifiers.getBankTransfer());
            case PaymentMethod.CHEQUE -> new ChequeTrxProcessor(paymentMethodModifiers.getCheque());
            default -> throw new RuntimeException("Unsupported payment method: " + paymentMethod);
        };
    }
}
