package api.handlers.reservations;

import api.business.entities.*;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IPaymentsService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.ErrorDto;
import api.contracts.enums.PaymentTypes;
import api.contracts.enums.PaymentsFrequency;
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
        Reservation reservation = createReservation(request, payment);

        response.payment = payment.getPaymentid();
        response.reservation = reservation.getReservationid();

        return response;
    }

    private Reservation createReservation(CreateReservationRequest request, Payment payment) {
        Reservation reservation = new Reservation();

        Cottage cottage = em.getById(Cottage.class, request.cottage);
        reservation.setCottage(cottage);

        User user = userService.get();
        reservation.setUser(user);

        reservation.setPayment(payment);

        return em.insert(reservation);
    }

    private Payment createPayment(CreateReservationRequest request) {
        Payment payment = setupPayment(request);
        setPendingPayment(payment);
        addRentLineItem(request, payment);

        payment.setAmount(payment.calculatePrice()); // TODO: Move to strategy (so CDI Decorators could possibly work)

        int id = paymentsService.createPayment(payment);
        return paymentsService.getPayment(id);
    }

    private Payment setupPayment(CreateReservationRequest request) {
        Payment payment = new Payment();
        payment.setActive(true);
        payment.setFrequencyId(PaymentsFrequency.once.getValue());
        payment.setPaymenttypeid(PaymentTypes.pay.getValue());
        PaymentsSettings settings = new PaymentsSettings();
        settings.setPaymentsettingsid(1);
        payment.setSettings(settings);
        payment.setCurrency("EUR");

        Cottage cottage = em.getById(Cottage.class, request.cottage);
        payment.setPaytext("Payment for cottage \"" + cottage.getTitle() + "\"");

        return payment;
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

    private void addRentLineItem(CreateReservationRequest request, Payment payment) {
        Collection<LineItem> lineItems = new ArrayList<>();

        Cottage cottage = em.getById(Cottage.class, request.cottage);

        LineItem rent = new LineItem();
        rent.setTitle("Rent for cottage \"" + cottage.getTitle() + "\"");
        rent.setPrice(50);
        rent.setQuantity(1);
        rent.setPayment(payment);
        lineItems.add(rent);

        payment.setLineItems(lineItems);
    }

    @Override
    public CreateReservationResponse createResponse() {
        return new CreateReservationResponse();
    }
}
