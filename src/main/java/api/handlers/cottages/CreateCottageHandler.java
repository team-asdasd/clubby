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
import java.util.Date;

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

        if (request.title.length() < 5) {
            errors.add(new ErrorDto("Title must be at least 5 characters long", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.image.length() < 1) {
            errors.add(new ErrorDto("Image url must be provided", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.beds <= 0) {
            errors.add(new ErrorDto("Bed count must be higher than zero.", ErrorCodes.VALIDATION_ERROR));
        }
        try {
            Date date = new Date(0, Integer.parseInt(request.availableFrom.split("-")[0]), Integer.parseInt(request.availableFrom.split("-")[1]));
        } catch (Exception e){
            errors.add(new ErrorDto("Incorrect date format", ErrorCodes.VALIDATION_ERROR));
        }


        return errors;
    }

    @Override
    public CreateCottageResponse handleBase(CreateCottageRequest request) {
        Cottage cottage = new Cottage();
        cottage.setTitle(request.title);
        cottage.setBedcount(request.beds);
        cottage.setImageurl(request.image);
        cottage.setDescription(request.description);
        Date dateFrom = new Date(0, Integer.parseInt(request.availableFrom.split("-")[0]), Integer.parseInt(request.availableFrom.split("-")[1]));
        cottage.setAvailableFrom(dateFrom);
        Date dateTo = new Date(0, Integer.parseInt(request.availableTo.split("-")[0]), Integer.parseInt(request.availableFrom.split("-")[1]));


        cottageService.save(cottage);
        CreateCottageResponse response = createResponse();
        response.cottage = new CottageDto(cottage);

        return response;
    }

    @Override
    public CreateCottageResponse createResponse() {
        return new CreateCottageResponse();
    }
}
