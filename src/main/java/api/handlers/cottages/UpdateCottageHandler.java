package api.handlers.cottages;

import api.business.entities.Cottage;
import api.business.services.interfaces.ICottageService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.cottages.UpdateCottageRequest;
import api.contracts.cottages.UpdateCottageResponse;
import api.contracts.dto.CottageDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class UpdateCottageHandler extends BaseHandler<UpdateCottageRequest, UpdateCottageResponse> {
    @Inject
    private ICottageService cottageService;

    @Override
    public ArrayList<ErrorDto> validate(UpdateCottageRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        ArrayList<ErrorDto> errors = Validator.checkAllNotNull(request);
        errors.addAll(Validator.checkAllNotNull(request.Cottage));

        if (!errors.isEmpty()) {
            return errors;
        }

        if (!currentUser.isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
        }

        if (!currentUser.hasRole("administrator")) {
            errors.add(new ErrorDto("Insufficient permissions.", ErrorCodes.AUTHENTICATION_ERROR));
        }

        if (cottageService.getCottage(request.Cottage.Id) == null) {
            errors.add(new ErrorDto("Cottage not found", ErrorCodes.NOT_FOUND));
        }

        return errors;
    }

    @Override
    public UpdateCottageResponse handleBase(UpdateCottageRequest request) {
        UpdateCottageResponse response = createResponse();

        Cottage cottage = cottageService.getCottage(request.Cottage.Id);

        cottage.setTitle(request.Cottage.Title);
        cottage.setBedcount(request.Cottage.Beds);
        cottage.setImageurl(request.Cottage.Image);

        cottageService.createCottage(cottage);

        response.Cottage = new CottageDto(cottage.getId(), cottage.getTitle(), cottage.getBedcount(), cottage.getImageurl());

        return response;
    }

    @Override
    public UpdateCottageResponse createResponse() {
        return new UpdateCottageResponse();
    }
}
