package api.handlers.users;

import api.business.entities.User;
import api.business.services.interfaces.IFormService;
import api.business.services.interfaces.ILoginService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.BaseRequest;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.users.GetUserInfoResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import clients.facebook.interfaces.IFacebookClient;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class GetCurrentUserHandler extends BaseHandler<BaseRequest, GetUserInfoResponse> {
    @Inject
    private IUserService userService;
    @Inject
    private ILoginService loginService;
    @Inject
    private IFacebookClient facebookClient;
    @Inject
    private IFormService formService;

    @Override
    public ArrayList<ErrorDto> validate(BaseRequest request) {
        ArrayList<ErrorDto> errors = Validator.checkAllNotNullAndIsAuthenticated(request);

        User user = userService.get();

        if (user == null) {
            errors.add(new ErrorDto("user not found", ErrorCodes.NOT_FOUND));
        }

        return errors;
    }

    @Override
    public GetUserInfoResponse handleBase(BaseRequest request) {

        User user = userService.get();

        GetUserInfoResponse response = createResponse();
        response.id = user.getId();
        response.name = user.getName();
        response.email = user.getLogin().getEmail();
        response.picture = user.getPicture();
        response.fields = formService.getFormByUserId(user.getId());

        return response;
    }

    @Override
    public GetUserInfoResponse createResponse() {
        return new GetUserInfoResponse();
    }
}
