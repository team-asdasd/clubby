package api.handlers.cottages;

import api.business.services.interfaces.ICottageService;
import api.contracts.cottages.GetCottagesResponse;
import api.contracts.cottages.GetCottagesRequest;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class GetCottagesHandler extends BaseHandler<GetCottagesRequest, GetCottagesResponse> {
    @Inject
    private ICottageService cottageService;

    @Override
    public ArrayList<ErrorDto> validate(GetCottagesRequest request) {
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
    public GetCottagesResponse handleBase(GetCottagesRequest request) {
        GetCottagesResponse response = createResponse();

        response.Cottages = cottageService.getAllCottages();

        return response;
    }

    @Override
    public GetCottagesResponse createResponse() {
        return new GetCottagesResponse();
    }
}
