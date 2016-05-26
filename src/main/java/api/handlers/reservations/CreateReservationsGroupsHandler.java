package api.handlers.reservations;

import api.business.entities.User;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IGroupsAssignmentService;
import api.contracts.base.BaseRequest;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class CreateReservationsGroupsHandler extends BaseHandler<BaseRequest, BaseResponse> {
    @Inject
    private ISimpleEntityManager em;
    @Inject
    private IGroupsAssignmentService gas;

    @Override
    public ArrayList<ErrorDto> validate(BaseRequest request) {
        ArrayList<ErrorDto> errors = new ArrayList<>();
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
            return errors;
        }

        if (!SecurityUtils.getSubject().hasRole("administrator")) {
            errors.add(new ErrorDto("User is not a administrator.", ErrorCodes.AUTHENTICATION_ERROR));
            return errors;
        }

        return errors;
    }

    @Override
    public BaseResponse handleBase(BaseRequest request) {
        List<User> users = em.getAll(User.class);

        gas.Assign(users);

        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
