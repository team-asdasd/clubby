package api.handlers.users;

import api.business.services.interfaces.ILoginService;
import api.business.services.interfaces.IUserService;
import api.contracts.requests.FreeEmailRequest;
import api.contracts.requests.FreeUsernameRequest;
import api.contracts.responses.FreeEmailResponse;
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
public class FreeEmailHandler extends BaseHandler<FreeEmailRequest, FreeEmailResponse> {

    @Inject
    private IUserService userService;

    @Override
    public ArrayList<ErrorDto> validate(FreeEmailRequest request) {
        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (request == null) {
            errors.add(new ErrorDto("Request missing.", ErrorCodes.VALIDATION_ERROR));
        }

        return errors;
    }

    @Override
    public FreeEmailResponse handleBase(FreeEmailRequest request) {
        FreeEmailResponse response = createResponse();

        response.FreeEmail = userService.getByEmail(request.Email) == null;

        return response;
    }

    @Override
    public FreeEmailResponse createResponse() {
        return new FreeEmailResponse();
    }
}
