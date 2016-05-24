package api.handlers.reservations;

import api.business.entities.*;
import api.business.persistance.ISimpleEntityManager;
import api.business.persistance.SimpleEntityManager;
import api.business.services.PaymentsService;
import api.business.services.interfaces.IPaymentsService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.ErrorDto;
import api.contracts.enums.PaymentTypes;
import api.contracts.enums.PaymentsFrequency;
import api.contracts.reservations.CreateReservationRequest;
import api.contracts.reservations.CreateReservationResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import com.google.api.client.util.SecurityUtils;

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
    @Inject
    private IUserService userService;

    @Override
    public ArrayList<ErrorDto> validate(CreateReservationRequest request) {
        return Validator.checkAllNotNullAndIsAuthenticated(request);
    }

    @Override
    public CreateReservationResponse handleBase(CreateReservationRequest request) {
        CreateReservationResponse response = createResponse();

        Payment payment = createPayment(request);
        Reservation reservation = createReservation(request);

        response.payment = payment.getPaymentid();
/*
        response.reservation = reservation.getReservationid();
*/

        return response;
    }

    private Reservation createReservation(CreateReservationRequest request) {
        return null;
    }

    private Payment createPayment(CreateReservationRequest request) {
        Payment payment = new Payment();
        payment.setActive(true);
        payment.setFrequencyId(PaymentsFrequency.once.getValue());
        payment.setPaymenttypeid(PaymentTypes.pay.getValue());
        PaymentsSettings settings = new PaymentsSettings();
        settings.setPaymentsettingsid(1);
        payment.setSettings(settings);
        payment.setCurrency("EUR");

        setPendingPayment(payment);

        Cottage cottage = em.getById(Cottage.class, request.cottage);

        payment.setPaytext("Payment for cottage \"" + cottage.getTitle() + "\"");

        Collection<LineItem> lineItems = getLineItems(request);
        payment.setLineItems(lineItems);

        int id = paymentsService.createPayment(payment);
        return paymentsService.getPayment(id);
    }

    private void setPendingPayment(Payment payment) {
        User current = userService.get();

        Collection<PendingPayment> pendingPayments = new ArrayList<>();

        PendingPayment pendingPayment = new PendingPayment();
        pendingPayment.setUserId(current.getId());
        pendingPayment.setPayment(payment);

        pendingPayments.add(pendingPayment);

        payment.setPendingPayments(pendingPayments);
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
