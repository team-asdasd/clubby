package api.handlers.users;

import api.business.entities.User;
import api.business.services.interfaces.IUserService;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.users.DisableUserRequest;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;

import javax.inject.Inject;
import java.util.ArrayList;

public class DisableUserHandler extends BaseHandler<DisableUserRequest, BaseResponse> {
    @Inject
    private IUserService userService;

    @Override
    public ArrayList<ErrorDto> validate(DisableUserRequest request) {
        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (!SecurityUtils.getSubject().isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
            return errors;
        }

        if (!SecurityUtils.getSubject().hasRole("administrator")) {
            errors.add(new ErrorDto("Permission denied.", ErrorCodes.AUTHENTICATION_ERROR));
            return errors;
        }

        User user = userService.get(request.id);
        if (user == null) {
            errors.add(new ErrorDto("User does not found", ErrorCodes.NOT_FOUND));
        }
        return errors;
    }

    @Override
    public BaseResponse handleBase(DisableUserRequest request) {

        userService.disableUser(request.id);
        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
