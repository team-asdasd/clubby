package api.business.decorators;

import api.business.entities.LineItem;
import api.business.entities.Payment;
import api.business.entities.User;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Random;

@Decorator
public class RandomGiftDecorator implements IPaymentModifier {
    @Inject
    @Delegate
    @Any
    private IPaymentModifier modifier;

    @Override
    public Payment modify(Payment payment, User user) {
        Random r = new Random();

        if (r.nextInt(100) < 50) {
            LineItem lineItem = new LineItem("Random discount", payment.calculatePrice() / -10, 1, payment);
            Collection<LineItem> lineItems = payment.getLineItems();
            lineItems.add(lineItem);
            payment.setLineItems(lineItems);
        }
        return modifier.modify(payment, user);
    }

}
