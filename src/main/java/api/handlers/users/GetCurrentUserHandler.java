package api.handlers.users;

import api.business.entities.Login;
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
import clients.facebook.responses.FacebookUserDetails;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;

@Stateless
public class GetCurrentUserHandler extends BaseHandler<BaseRequest, GetUserInfoResponse> {
    @Inject
    private IUserService userInfoService;
    @Inject
    private ILoginService loginService;
    @Inject
    private IFacebookClient facebookClient;
    @Inject
    private IFormService formService;

    @Override
    public ArrayList<ErrorDto> validate(BaseRequest request) {
        return Validator.checkAllNotNullAndIsAuthenticated(request);
    }

    @Override
    public GetUserInfoResponse handleBase(BaseRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        GetUserInfoResponse response = createResponse();
        String username = currentUser.getPrincipal().toString();

        Login login = loginService.getByUserName(username);
        User user = login.getUser();

        response.email = login.getUsername();
        if (user == null) {
            logger.warn(String.format("User %s not found", username));
            return handleException("User not found", ErrorCodes.NOT_FOUND);
        }
        
        response.id = user.getId();
        response.name = user.getName();
        response.picture = user.getPicture();
        response.fields = formService.getFormByUserId(user.getId());

        return response;
    }

    @Override
    public GetUserInfoResponse createResponse() {
        return new GetUserInfoResponse();
    }
}
