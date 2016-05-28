package api.handlers.users;

import api.business.entities.User;
import api.business.services.interfaces.IUserService;
import api.contracts.base.BaseRequest;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class DisableMeHandler extends BaseHandler<BaseRequest, BaseResponse> {
    @Inject
    private IUserService userService;

    @Override
    public ArrayList<ErrorDto> validate(BaseRequest request) {
        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (!SecurityUtils.getSubject().isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
            return errors;
        }

        return errors;
    }

    @Override
    public BaseResponse handleBase(BaseRequest request) {
        User user = userService.get();
        userService.disableUser();
        userService.logoutUser(user.getId());
        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
