package api.handlers;

import api.business.entities.Cottage;
import api.business.entities.LineItem;
import api.business.entities.Payment;
import api.business.entities.PaymentsSettings;
import api.business.persistance.ISimpleEntityManager;
import api.business.persistance.SimpleEntityManager;
import api.business.services.PaymentsService;
import api.business.services.interfaces.IPaymentsService;
import api.contracts.base.ErrorDto;
import api.contracts.reservations.CreateReservationRequest;
import api.contracts.reservations.CreateReservationResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;

@Stateless
public class CreateReservationHandler extends BaseHandler<CreateReservationRequest, CreateReservationResponse> {
    @Inject
    private ISimpleEntityManager em;
    @Inject
    private IPaymentsService paymentsService;

    @Override
    public ArrayList<ErrorDto> validate(CreateReservationRequest request) {
        return Validator.checkAllNotNullAndIsAuthenticated(request);
    }

    @Override
    public CreateReservationResponse handleBase(CreateReservationRequest request) {
        CreateReservationResponse response = createResponse();

        Payment payment = createPayment(request);

        response.payment = payment.getPaymentid();

        return response;
    }

    private Payment createPayment(CreateReservationRequest request) {
        Payment payment = new Payment();
        payment.setActive(true);
        payment.setFrequencyId(3);
        payment.setPaymenttypeid(2);
        payment.setCurrency("EUR");

        Cottage cottage = em.getById(Cottage.class, request.cottage);

        payment.setPaytext("Payment for cottage\"" + cottage.getTitle() + "\"");

        Collection<LineItem> lineItems = getLineItems(request);
        payment.setLineItems(lineItems);

        payment.setAmount(payment.calculatePrice());

        int id = paymentsService.createPayment(payment);
        return paymentsService.getPayment(id);
    }

    private Collection<LineItem> getLineItems(CreateReservationRequest request) {
        Collection<LineItem> lineItems = new ArrayList<>();

        Cottage cottage = em.getById(Cottage.class, request.cottage);

        LineItem rent = new LineItem();
        rent.setTitle("Rent for " + cottage.getTitle());
        rent.setPrice(50);
        rent.setQuantity(1);

        lineItems.add(rent);

        return lineItems;
    }

    @Override
    public CreateReservationResponse createResponse() {
        return new CreateReservationResponse();
    }
}
