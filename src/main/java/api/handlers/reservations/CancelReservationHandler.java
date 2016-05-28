package api.handlers.reservations;

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

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class CancelReservationHandler extends BaseHandler<CancelReservationRequest, BaseResponse> {
    @Inject
    private ISimpleEntityManager em;
    @Inject
    private IUserService userService;
    @Inject
    private ICottageService cottageService;

    @Override
    public ArrayList<ErrorDto> validate(CancelReservationRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAuthenticated().getErrors();

        if (!authErrors.isEmpty()) return authErrors;

        ArrayList<ErrorDto> errors = new Validator().isMember().getErrors();

        Reservation reservation = em.getById(Reservation.class, request.id);
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

        // TODO Check if cancellation deadline is met
                /*if(status == TransactionStatus.cancelled.getValue() || status == TransactionStatus.cancelled.getValue())*/

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
}
