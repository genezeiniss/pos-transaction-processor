package com.genezeiniss.pos_transaction_processor.service.transaction_processor;

import com.genezeiniss.pos_transaction_processor.domain.enums.PaymentMethod;
import com.genezeiniss.pos_transaction_processor.domain.payment_method_modifiers.PaymentMethodModifiers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionProcessorFactoryTest {

    private final TransactionProcessorFactory factory = new TransactionProcessorFactory(new PaymentMethodModifiers());

    @ParameterizedTest
    @EnumSource(PaymentMethod.class)
    @DisplayName("get transaction processor for each payment method")
    void getTransactionProcessor(PaymentMethod paymentMethod) {

        if (paymentMethod == PaymentMethod.UNKNOWN) {
            return;
        }

        TransactionProcessor processor = factory.getTransactionProcessor(paymentMethod);

        switch (paymentMethod) {
            case CASH -> assertInstanceOf(CashTrxProcessor.class, processor);
            case CASH_ON_DELIVERY -> assertInstanceOf(CashOnDeliveryTrxProcessor.class, processor);
            case VISA -> assertInstanceOf(VisaTrxProcessor.class, processor);
            case MASTERCARD -> assertInstanceOf(MastercardTrxProcessor.class, processor);
            case AMEX -> assertInstanceOf(AmexTrxProcessor.class, processor);
            case JCB -> assertInstanceOf(JcbTrxProcessor.class, processor);
            case LINE_PAY -> assertInstanceOf(LinePayTrxProcessor.class, processor);
            case PAYPAY -> assertInstanceOf(PaypayTrxProcessor.class, processor);
            case POINTS -> assertInstanceOf(PointsTrxProcessor.class, processor);
            case GRAB_PAY -> assertInstanceOf(GrabPayTrxProcessor.class, processor);
            case BANK_TRANSFER -> assertInstanceOf(BankTransferTrxProcessor.class, processor);
            case CHEQUE -> assertInstanceOf(ChequeTrxProcessor.class, processor);
            default -> fail("Unexpected payment method: " + paymentMethod);
        }
    }

    @Test
    @DisplayName("get transaction processor for unsupported payment method")
    void getTransactionProcessorForUnsupportedMethod() {
        PaymentMethod unsupportedMethod = PaymentMethod.UNKNOWN;
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> factory.getTransactionProcessor(unsupportedMethod));
        assertEquals("Unsupported payment method: " + unsupportedMethod, exception.getMessage());
    }
}
