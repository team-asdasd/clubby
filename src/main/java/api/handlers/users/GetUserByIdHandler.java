package api.handlers.users;

import api.business.entities.User;
import api.business.services.interfaces.IFormService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.requests.GetUserByIdRequest;
import api.contracts.responses.GetUserByIdResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import clients.facebook.interfaces.IFacebookClient;
import clients.facebook.responses.FacebookUserDetails;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;

@Stateless
public class GetUserByIdHandler extends BaseHandler<GetUserByIdRequest, GetUserByIdResponse> {
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
    public GetUserByIdResponse handleBase(GetUserByIdRequest request) {
        GetUserByIdResponse response = createResponse();
        User user = userInfoService.get(request.Id);

        if (user == null) {
            logger.warn(String.format("User %s not found", request.Id));
            return handleException("User not found", ErrorCodes.NOT_FOUND);
        }

        if (user.isFacebookUser()) {
            try {
                FacebookUserDetails userDetails = facebookClient.getUserDetailsById(user.getFacebookId());
                if (!userDetails.Picture.isSilhouette()) {
                    response.Picture = userDetails.Picture.getUrl();
                }
            } catch (IOException e) {
                handleException(e);
            }
        }
        response.formInfo = formService.getFormByUserId(request.Id);
        response.Name = user.getName();

        return response;
    }

    @Override
    public GetUserByIdResponse createResponse() {
        return new GetUserByIdResponse();
    }
}
