package api.business.decorators;

import api.business.entities.Payment;
import api.business.entities.User;

public interface IPaymentModifier {
    Payment modify(Payment payment, User user);
}
