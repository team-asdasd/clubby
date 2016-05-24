package api.handlers.cottages;

import api.business.entities.Cottage;
import api.business.services.interfaces.ICottageService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.cottages.GetCottageRequest;
import api.contracts.cottages.GetCottageResponse;
import api.contracts.dto.CottageDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class GetCottageHandler extends BaseHandler<GetCottageRequest, GetCottageResponse> {
    @Inject
    private ICottageService cottageService;

    @Override
    public ArrayList<ErrorDto> validate(GetCottageRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        ArrayList<ErrorDto> errors = Validator.checkAllNotNull(request);

        if (!currentUser.isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
        }

        if (cottageService.get(request.id) == null) {
            errors.add(new ErrorDto("cottage not found", ErrorCodes.NOT_FOUND));
        }

        return errors;
    }

    @Override
    public GetCottageResponse handleBase(GetCottageRequest request) {
        GetCottageResponse response = createResponse();

        Cottage cottage = cottageService.get(request.id);

        response.cottage = new CottageDto(cottage);

        return response;
    }

    @Override
    public GetCottageResponse createResponse() {
        return new GetCottageResponse();
    }
}
