package api.handlers.reservations;

import api.business.entities.Configuration;
import api.business.entities.Reservation;
import api.business.entities.User;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.ICottageService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.reservations.CancelReservationRequest;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
import org.joda.time.DateTime;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

@Stateless
public class CancelReservationHandler extends BaseHandler<CancelReservationRequest, BaseResponse> {
    @PersistenceContext
    private EntityManager em;
    @Inject
    private ISimpleEntityManager sem;
    @Inject
    private IUserService userService;
    @Inject
    private ICottageService cottageService;

    @Override
    public ArrayList<ErrorDto> validate(CancelReservationRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isMember().getErrors();

        if (!authErrors.isEmpty()) return authErrors;

        ArrayList<ErrorDto> errors = new Validator().getErrors();

        Reservation reservation = sem.getById(Reservation.class, request.id);
        if (reservation == null) {
            errors.add(new ErrorDto("Reservation not found.", ErrorCodes.NOT_FOUND));
            return errors;
        }

        User current = userService.get();
        if (reservation.getUser().getId() != current.getId()) {
            errors.add(new ErrorDto("Reservation does not belong to this user.", ErrorCodes.ACCESS_DENIED));
            return errors;
        }

        if (reservation.getCancelled()) {
            errors.add(new ErrorDto("Reservation is already cancelled.", ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        DateTime dateFrom = new DateTime(reservation.getDateFrom());
        int daysBefore = daysBeforeCancellationPeriodEnds();
        DateTime cancellationPeriodEnd = dateFrom.minusDays(daysBefore);
        if (cancellationPeriodEnd.isBefore(DateTime.now())) {
            errors.add(new ErrorDto(String.format("Reservation cannot be cancelled %d days until it starts.", daysBefore), ErrorCodes.VALIDATION_ERROR));
            return errors;
        }

        return errors;
    }

    @Override
    public BaseResponse handleBase(CancelReservationRequest request) {
        BaseResponse response = createResponse();

        boolean cancelled = cottageService.cancelReservation(request.id);
        if (!cancelled) {
            response.Errors.add(new ErrorDto("Reservation could not be canceled", ErrorCodes.GENERAL_ERROR));
        }

        return response;
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }

    public int daysBeforeCancellationPeriodEnds() {
        int defaultDays = 14;

        Configuration configuration = em.find(Configuration.class, "days_before_cancellation_period_ends");
        if (configuration == null) {
            return defaultDays;
        }

        String setting = configuration.getValue();
        if (setting == null) {
            return defaultDays;
        }

        try {
            return Integer.parseInt(setting);
        } catch (Exception e) {
            return defaultDays;
        }
    }
}
