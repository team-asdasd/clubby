package api.handlers.reservations;

import api.business.entities.*;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IPaymentsService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.enums.PaymentTypes;
import api.contracts.enums.PaymentsFrequency;
import api.contracts.reservations.CreateReservationRequest;
import api.contracts.reservations.CreateReservationResponse;
import api.contracts.reservations.services.ServiceSelectionDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.shiro.SecurityUtils;

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
        ArrayList<ErrorDto> errors = new ArrayList<>();
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
            return errors;
        }

        if (request.cottage <= 0) {
            errors.add(new ErrorDto("Invalid cottage id.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        Cottage cottage = em.getById(Cottage.class, request.cottage);
        if (cottage == null) {
            errors.add(new ErrorDto("Cottage not found.", ErrorCodes.NOT_FOUND));
            return errors;
        }

        if (request.from == null) {
            errors.add(new ErrorDto("Date from must be provided.", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.to == null) {
            errors.add(new ErrorDto("Date to must be provided.", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.services != null) {
            for (ServiceSelectionDto serviceSelection : request.services) {
                if (serviceSelection.id <= 0) {
                    errors.add(new ErrorDto("Invalid service id.", ErrorCodes.VALIDATION_ERROR));
                    return errors;
                }

                if (serviceSelection.amount <= 0) {
                    errors.add(new ErrorDto("Invalid service amount.", ErrorCodes.VALIDATION_ERROR));
                    return errors;
                }

                Service service = em.getById(Service.class, serviceSelection.id);
                if (service == null) {
                    errors.add(new ErrorDto("Service not found.", ErrorCodes.NOT_FOUND));
                    return errors;
                }

                if (request.cottage != service.getCottage().getId()) {
                    errors.add(new ErrorDto("Selected service does not belong to selected cottage.", ErrorCodes.VALIDATION_ERROR));
                    return errors;
                }

                if (serviceSelection.amount > service.getMaxCount()) {
                    errors.add(new ErrorDto("Service amount selection exceeds available service limit.", ErrorCodes.VALIDATION_ERROR));
                    return errors;
                }
            }
        }

        return errors;
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
        rent.setPrice(cottage.getPrice());
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
