package api.handlers.cottages;

import api.business.entities.Cottage;
import api.business.entities.Service;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.cottages.CreateCottageRequest;
import api.contracts.cottages.CreateCottageResponse;
import api.contracts.dto.CottageDto;
import api.contracts.dto.ServiceDto;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Stateless
public class CreateCottageHandler extends BaseHandler<CreateCottageRequest, CreateCottageResponse> {
    @PersistenceContext
    private EntityManager em;

    @Override
    public ArrayList<ErrorDto> validate(CreateCottageRequest request) {
        Subject currentUser = SecurityUtils.getSubject();
        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (!currentUser.isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
        }

        if (!currentUser.hasRole("administrator")) {
            errors.add(new ErrorDto("Insufficient permissions.", ErrorCodes.AUTHENTICATION_ERROR));
        }

        if (request.title == null || request.title.length() < 5) {
            errors.add(new ErrorDto("Title must be at least 5 characters long", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.image == null || request.image.length() < 1) {
            errors.add(new ErrorDto("Image url must be provided", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.beds <= 0) {
            errors.add(new ErrorDto("Bed count must be higher than zero.", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.description == null || request.description.isEmpty()) {
            errors.add(new ErrorDto("Description must be provided.", ErrorCodes.VALIDATION_ERROR));
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            sdf.setLenient(false);
            Date date = sdf.parse(request.availableFrom);
            Date date2 = sdf.parse(request.availableTo);
        } catch (ParseException e) {
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
        cottage.setPrice(request.price);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        sdf.setLenient(false);
        try {
            cottage.setAvailableFrom(sdf.parse(request.availableFrom));
            cottage.setAvailableTo(sdf.parse(request.availableTo));
        } catch (ParseException ignored) {

        }

        em.persist(cottage);

        if (request.services != null) {
            for (ServiceDto dto : request.services) {
                Service service = new Service();
                service.setDescription(dto.description);
                service.setCottage(cottage);
                service.setMaxCount(dto.maxCount);
                service.setPrice(dto.price);
                em.persist(service);
            }
        }

        CreateCottageResponse response = createResponse();
        response.cottage = new CottageDto(cottage);

        return response;
    }

    @Override
    public CreateCottageResponse createResponse() {
        return new CreateCottageResponse();
    }
}
