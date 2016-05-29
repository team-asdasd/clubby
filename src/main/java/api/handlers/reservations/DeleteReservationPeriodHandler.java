package api.handlers.reservations;

import api.business.entities.ReservationsPeriod;
import api.business.persistance.ISimpleEntityManager;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorDto;
import api.contracts.reservations.DeleteReservationPeriodRequest;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class DeleteReservationPeriodHandler extends BaseHandler<DeleteReservationPeriodRequest, BaseResponse> {
    @Inject
    private ISimpleEntityManager simpleEntityManager;

    @Override
    public ArrayList<ErrorDto> validate(DeleteReservationPeriodRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAuthenticated().getErrors();

        if (!authErrors.isEmpty()) return authErrors;

        return new Validator().isAdministrator().allFieldsSet(request).isValidId(request.id).getErrors();
    }

    @Override
    public BaseResponse handleBase(DeleteReservationPeriodRequest request) {
        ReservationsPeriod rp = simpleEntityManager.getById(ReservationsPeriod.class, request.id);

        if (rp != null) {
            simpleEntityManager.delete(rp);
        }

        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
