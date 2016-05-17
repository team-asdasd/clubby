package api.handlers.users;

import api.business.entities.User;
import api.business.services.interfaces.IFormService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.users.GetUserByIdRequest;
import api.contracts.users.GetUserInfoResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import clients.facebook.interfaces.IFacebookClient;
import clients.facebook.responses.FacebookUserDetails;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;

@Stateless
public class GetUserByIdHandler extends BaseHandler<GetUserByIdRequest, GetUserInfoResponse> {
    @Inject
    private IUserService userInfoService;
    @Inject
    private IFacebookClient facebookClient;
    @Inject
    private IFormService formService;

    @Override
    public ArrayList<ErrorDto> validate(GetUserByIdRequest request) {

        ArrayList<ErrorDto> errors = Validator.checkAllNotNullAndIsAuthenticated(request);

        return errors;
    }

    @Override
    public GetUserInfoResponse handleBase(GetUserByIdRequest request) {
        GetUserInfoResponse response = createResponse();
        User user = userInfoService.get(request.id);

        if (user == null) {
            logger.warn(String.format("User %s not found", request.id));
            return handleException("User not found", ErrorCodes.NOT_FOUND);
        }

        if (user.isFacebookUser()) {
            try {
                FacebookUserDetails userDetails = facebookClient.getUserDetailsById(user.getFacebookId());
                if (!userDetails.Picture.isSilhouette()) {
                    response.picture = userDetails.Picture.getUrl();
                }
            } catch (IOException e) {
                handleException(e);
            }
        }

        response.fields = formService.getFormByUserId(request.id);
        response.id = user.getId();
        response.email = user.getLogin().getEmail();
        response.name = user.getName();

        return response;
    }

    @Override
    public GetUserInfoResponse createResponse() {
        return new GetUserInfoResponse();
    }
}
