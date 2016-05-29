package api.handlers.users;

import api.business.entities.User;
import api.business.services.interfaces.IUserService;
import api.contracts.base.BaseRequest;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
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
        return new Validator().isAuthenticated().getErrors();
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
