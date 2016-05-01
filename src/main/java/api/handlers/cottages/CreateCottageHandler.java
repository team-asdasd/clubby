package api.handlers.cottages;

import api.business.entities.Cottage;
import api.business.services.interfaces.ICottageService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.cottages.CreateCottageRequest;
import api.contracts.cottages.CreateCottageResponse;
import api.contracts.dto.CottageDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class CreateCottageHandler extends BaseHandler<CreateCottageRequest, CreateCottageResponse> {
    @Inject
    private ICottageService cottageService;

    @Override
    public ArrayList<ErrorDto> validate(CreateCottageRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        ArrayList<ErrorDto> errors = Validator.checkAllNotNull(request);

        if (!currentUser.isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
        }

        if (!currentUser.hasRole("administrator")) {
            errors.add(new ErrorDto("Insufficient permissions.", ErrorCodes.AUTHENTICATION_ERROR));
        }

        return errors;
    }

    @Override
    public CreateCottageResponse handleBase(CreateCottageRequest request) {
        Cottage cottage = new Cottage();
        cottage.setTitle(request.title);
        cottage.setBedcount(request.bedcount);
        cottage.setImageurl(request.imageurl);

        cottageService.save(cottage);
        CreateCottageResponse response = createResponse();
        response.Cottage = new CottageDto(cottage);

        return response;
    }

    @Override
    public CreateCottageResponse createResponse() {
        return new CreateCottageResponse();
    }
}
