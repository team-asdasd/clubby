package api.handlers.users;

import api.business.entities.User;
import api.business.services.interfaces.IUserService;
import api.contracts.requests.GetUserByIdRequest;
import api.contracts.responses.GetUserByIdResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;
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
public class GetUserByIdHandler extends BaseHandler<GetUserByIdRequest, GetUserByIdResponse> {

    @Inject
    private IUserService userInfoService;
    @Inject
    private IFacebookClient facebookClient;

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
            logger.warn(String.format("User ? not found", request.Id));
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

        response.Name = user.getName();

        return response;
    }

    @Override
    public GetUserByIdResponse createResponse() {
        return new GetUserByIdResponse();
    }
}
