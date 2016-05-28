package api.handlers.reservations;

import api.business.entities.*;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.ICottageService;
import api.business.services.interfaces.IPaymentsService;
import api.business.services.interfaces.IUserService;
import api.business.strategy.IPaymentModifierStrategy;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.dto.PaymentInfoDto;
import api.contracts.enums.PaymentTypes;
import api.contracts.enums.PaymentsFrequency;
import api.contracts.reservations.CreateReservationRequest;
import api.contracts.reservations.CreateReservationResponse;
import api.contracts.reservations.services.ServiceSelectionDto;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
import org.joda.time.*;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.*;

@Stateless
public class CreateReservationHandler extends BaseHandler<CreateReservationRequest, CreateReservationResponse> {
    @Inject
    private ISimpleEntityManager em;
    @Inject
    private IPaymentsService paymentsService;
    @Inject
    private ICottageService cottageService;
    @Inject
    private IUserService userService;
    /*
    @Inject
    private IPaymentModifierStrategy paymentModifierStrategy;
    */

    @Override
    public ArrayList<ErrorDto> validate(CreateReservationRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAuthenticated().getErrors();

        if (!authErrors.isEmpty()) return authErrors;

        ArrayList<ErrorDto> errors = new Validator().isMember().getErrors();

        if (!cottageService.isNowReservationPeriod()) {
            errors.add(new ErrorDto("Now is not reservation period.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        User user = userService.get();
        List<PaymentInfoDto> pendingPaymentsForUser = paymentsService.getPendingPaymentsForUser(user.getId());
        boolean hasDebts = pendingPaymentsForUser.stream().anyMatch(p -> p.Required);

        if (hasDebts) {
            errors.add(new ErrorDto("User has unpaid required payments.", ErrorCodes.VALIDATION_ERROR));
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

        String fromDateString = request.from;
        if (fromDateString == null) {
            errors.add(new ErrorDto("Date 'from' must be provided.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        String toDateString = request.to;
        if (toDateString == null) {
            errors.add(new ErrorDto("Date 'to' must be provided.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        LocalDate from = parseLocalDate(fromDateString);
        if (from == null) {
            errors.add(new ErrorDto("Invalid 'from' date format.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        if (from.getDayOfWeek() != DateTimeConstants.MONDAY) {
            errors.add(new ErrorDto("Date 'from' must be Monday.", ErrorCodes.VALIDATION_ERROR));
        }

        LocalDate to = parseLocalDate(toDateString);
        if (to == null) {
            errors.add(new ErrorDto("Invalid 'to' date format.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        if (to.getDayOfWeek() != DateTimeConstants.SUNDAY) {
            errors.add(new ErrorDto("Date 'to' must be Sunday.", ErrorCodes.VALIDATION_ERROR));
        }

        LocalDate today = LocalDate.now();
        if (from.isBefore(today)) {
            errors.add(new ErrorDto("Date 'from' must be in the future.", ErrorCodes.VALIDATION_ERROR));
        }

        if (to.isBefore(today)) {
            errors.add(new ErrorDto("Date 'to' must be in the future.", ErrorCodes.VALIDATION_ERROR));
        }

        if (from.isAfter(to)) {
            errors.add(new ErrorDto("Date 'to' must be after 'from'.", ErrorCodes.VALIDATION_ERROR));
        }

        List<Cottage> availableCottages = cottageService.getAvailableCottagesForFullPeriod(from, to);
        boolean cottageAvailable = availableCottages.stream().anyMatch(c -> c.getId() == request.cottage);

        if (!cottageAvailable) {
            errors.add(new ErrorDto("Cottage is not available in that period.", ErrorCodes.VALIDATION_ERROR));
            return errors;
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

    private LocalDate parseLocalDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
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

        LocalDate from = LocalDate.parse(request.from);
        LocalDate to = LocalDate.parse(request.to);

        reservation.setDateFrom(from.toDate());
        reservation.setDateTo(to.toDate());

        Cottage cottage = em.getById(Cottage.class, request.cottage);
        reservation.setCottage(cottage);
        reservation.setCreated(DateTime.now(DateTimeZone.UTC).toDate());

        User user = userService.get();
        reservation.setUser(user);

        reservation.setPayment(payment);

        return em.insert(reservation);
    }

    private Payment createPayment(CreateReservationRequest request) {
        Payment payment = setupPayment(request);
        setPendingPayment(payment);
        addLineItems(request, payment);

        int id = paymentsService.createPayment(payment);
        return paymentsService.getPayment(id);
    }

    private Payment setupPayment(CreateReservationRequest request) {
        Cottage cottage = em.getById(Cottage.class, request.cottage);

        Payment payment = new Payment();
        payment.setPaytext("Payment for cottage \"" + cottage.getTitle() + "\"");
        payment.setActive(true);
        payment.setRequired(false);
        payment.setFrequencyId(PaymentsFrequency.once.getValue());
        payment.setPaymenttypeid(PaymentTypes.pay.getValue());
        PaymentsSettings settings = new PaymentsSettings();
        settings.setPaymentsettingsid(1);
        payment.setSettings(settings);
        payment.setCurrency("EUR");

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

    private void addLineItems(CreateReservationRequest request, Payment payment) {
        Collection<LineItem> lineItems = new ArrayList<>();

        Cottage cottage = em.getById(Cottage.class, request.cottage);

        int weeks = calculateWeeks(request);

        LineItem rent = new LineItem("Weekly rent for cottage \"" + cottage.getTitle() + "\"", cottage.getPrice(), weeks, payment);
        lineItems.add(rent);

        if (request.services != null) {
            for (ServiceSelectionDto serviceSelection : request.services) {
                Service service = em.getById(Service.class, serviceSelection.id);
                lineItems.add(new LineItem("Payment for service \"" + service.getDescription() + "\"", service.getPrice(), serviceSelection.amount, payment));
            }
        }

        /*paymentModifierStrategy.modify(payment);*/

        payment.setLineItems(lineItems);
    }

    private int calculateWeeks(CreateReservationRequest request) {
        LocalDate from = LocalDate.parse(request.from);
        LocalDate to = LocalDate.parse(request.to).plusDays(1); // Add one day, so weekly periods are full-weeks.

        return Weeks.weeksBetween(from, to).getWeeks();
    }

    @Override
    public CreateReservationResponse createResponse() {
        return new CreateReservationResponse();
    }
}
