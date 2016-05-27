package api.handlers.reservations;

import api.business.entities.ReservationsPeriod;
import api.business.persistance.ISimpleEntityManager;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.reservations.DeleteReservationPeriodRequest;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.shiro.SecurityUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class DeleteReservationPeriodHandler extends BaseHandler<DeleteReservationPeriodRequest, BaseResponse> {
    @Inject
    private ISimpleEntityManager simpleEntityManager;

    @Override
    public ArrayList<ErrorDto> validate(DeleteReservationPeriodRequest request) {
        ArrayList<ErrorDto> errors = Validator.checkAllNotNullAndIsAuthenticated(request);

        if (!SecurityUtils.getSubject().hasRole("administrator")) {
            errors.add(new ErrorDto("User is not a administrator.", ErrorCodes.UNAUTHENTICATED));
            return errors;
        }

        return errors;
    }

    @Override
    public BaseResponse handleBase(DeleteReservationPeriodRequest request) {
        ReservationsPeriod rp = simpleEntityManager.getById(ReservationsPeriod.class, request.id);

        if(rp != null){
            simpleEntityManager.delete(rp);
        }

        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
