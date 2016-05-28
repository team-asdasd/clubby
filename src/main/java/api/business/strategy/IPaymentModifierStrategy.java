package api.business.strategy;

import api.business.entities.Payment;

public interface IPaymentModifierStrategy {
    void modify(Payment payment);
}
