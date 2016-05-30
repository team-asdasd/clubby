package api.business.decorators;

import api.business.entities.Payment;
import api.business.entities.User;

public class PaymentModifier implements IPaymentModifier {

    @Override
    public Payment modify(Payment payment, User user) {
        return payment;
    }
}
