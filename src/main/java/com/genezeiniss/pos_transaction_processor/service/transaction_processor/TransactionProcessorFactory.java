package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.configuration.*;
import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import org.springframework.stereotype.Component;

@Component
public class TransactionProcessorFactory {

    public TransactionProcessor getTransactionProcessor(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case PaymentMethod.CASH -> new CashTrxProcessor(new CashProperties());
            case PaymentMethod.CASH_ON_DELIVERY -> new CashOnDeliveryTrxProcessor(new CashOnDeliveryProperties());
            case PaymentMethod.VISA -> new VisaTrxProcessor(new VisaProperties());
            case PaymentMethod.MASTERCARD -> new MastercardTrxProcessor(new MastercardProperties());
            case PaymentMethod.AMEX -> new AmexTrxProcessor(new AmexProperties());
            case PaymentMethod.JCB -> new JcbTrxProcessor(new JcbProperties());
            case PaymentMethod.LINE_PAY -> new LinePayTrxProcessor(new LinePayProperties());
            case PaymentMethod.PAYPAY -> new PaypayTrxProcessor(new PaypayProperties());
            case PaymentMethod.POINTS -> new PointsTrxProcessor(new PointsProperties());
            case PaymentMethod.GRAB_PAY -> new GrabPayTrxProcessor(new GrabPayProperties());
            case PaymentMethod.BANK_TRANSFER -> new BankTransferTrxProcessor(new BankTransferProperties());
            case PaymentMethod.CHEQUE -> new ChequeTrxProcessor(new ChequeProperties());
            default -> throw new RuntimeException("Unsupported payment method: " + paymentMethod);
        };
    }
}
