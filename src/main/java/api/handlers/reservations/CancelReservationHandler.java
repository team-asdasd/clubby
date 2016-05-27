package api.handlers.reservations;

import api.business.entities.Reservation;
import api.business.entities.User;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IUserService;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.reservations.CancelReservationRequest;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;

import javax.inject.Inject;
import java.util.ArrayList;

public class CancelReservationHandler extends BaseHandler<CancelReservationRequest, BaseResponse> {
    @Inject
    private ISimpleEntityManager em;

    @Inject
    private IUserService userService;

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

        return errors;
    }

    @Override
    public BaseResponse handleBase(CancelReservationRequest request) {
        return null;
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
