package api.handlers.users;

import api.business.entities.User;
import api.business.services.interfaces.ILoginService;
import api.business.services.EmailService;
import api.business.services.interfaces.IUserService;
import api.contracts.requests.GetUserInfoRequest;
import api.contracts.responses.GetUserInfoResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;
import api.handlers.base.BaseHandler;
import clients.facebook.interfaces.IFacebookClient;
import clients.facebook.responses.FacebookUserDetails;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;

@Stateless
public class GetUserInfoHandler extends BaseHandler<GetUserInfoRequest, GetUserInfoResponse> {

    @Inject
    private IUserService userInfoService;
    @Inject
    private ILoginService loginService;
    @Inject
    private IFacebookClient facebookClient;

    @Override
    public ArrayList<ErrorDto> validate(GetUserInfoRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (request == null) {
            errors.add(new ErrorDto("Request missing.", ErrorCodes.VALIDATION_ERROR));
        }

        if (!currentUser.isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
        }

        return errors;
    }

    @Override
    public GetUserInfoResponse handleBase(GetUserInfoRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        GetUserInfoResponse response = createResponse();
        String username = currentUser.getPrincipal().toString();

        User user = loginService.getByUserName(username).getUser();
        response.Email = user.getEmail();
        if (user == null) {
            logger.warn(String.format("User ? not found", username));
            return handleException("User not found", ErrorCodes.NOT_FOUND);
        }

        if (user.isFacebookUser()) {
            try {
                FacebookUserDetails userDetails = facebookClient.getUserDetails();
                if (!userDetails.Picture.isSilhouette()) {
                    response.Picture = userDetails.Picture.getUrl();
                }
            } catch (IOException e) {
                handleException(e);
            }
        }

        response.Name = user.getName();

        return response;
    }

    @Override
    public GetUserInfoResponse createResponse() {
        return new GetUserInfoResponse();
    }
}
