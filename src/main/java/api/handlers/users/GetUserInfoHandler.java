package api.handlers.users;

import api.business.User;
import api.business.services.interfaces.IUserInfoService;
import api.contracts.requests.GetUserInfoRequest;
import api.contracts.responses.GetUserInfoResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class GetUserInfoHandler extends BaseHandler<GetUserInfoRequest, GetUserInfoResponse> {
    @Inject
    private IUserInfoService userInfoService;

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

        // TODO: Map to business


        User user = userInfoService.get(1);


        response.Username = currentUser.getPrincipal().toString();
        response.Name = user.getName();
        response.Email = user.getEmail();

        return response;
    }

    @Override
    public GetUserInfoResponse createResponse() {
        return new GetUserInfoResponse();
    }
}
