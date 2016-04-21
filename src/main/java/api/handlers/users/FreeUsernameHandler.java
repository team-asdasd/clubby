package api.handlers.users;

import api.business.services.interfaces.ILoginService;
import api.business.services.interfaces.IUserService;
import api.contracts.requests.FreeUsernameRequest;
import api.contracts.responses.FreeUsernameResponse;
import api.contracts.responses.base.ErrorCodes;
import api.contracts.responses.base.ErrorDto;
import api.handlers.base.BaseHandler;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

/**
 * Created by Mindaugas on 21/04/2016.
 */
@Stateless
public class FreeUsernameHandler extends BaseHandler<FreeUsernameRequest, FreeUsernameResponse> {

    @Inject
    private ILoginService loginService;

    @Override
    public ArrayList<ErrorDto> validate(FreeUsernameRequest request) {
        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (request == null) {
            errors.add(new ErrorDto("Request missing.", ErrorCodes.VALIDATION_ERROR));
        }

        return errors;
    }

    @Override
    public FreeUsernameResponse handleBase(FreeUsernameRequest request) {
        FreeUsernameResponse response = createResponse();

        response.FreeUserName = loginService.isFreeUserName(request.UserName);

        return response;
    }

    @Override
    public FreeUsernameResponse createResponse() {
        return new FreeUsernameResponse();
    }
}
