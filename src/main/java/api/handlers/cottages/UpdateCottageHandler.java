package api.handlers.cottages;

import api.business.entities.Cottage;
import api.business.entities.Service;
import api.business.services.interfaces.ICottageService;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.cottages.UpdateCottageRequest;
import api.contracts.cottages.UpdateCottageResponse;
import api.contracts.dto.CottageDto;
import api.contracts.dto.ExistingServiceDto;
import api.contracts.dto.ServiceDto;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class UpdateCottageHandler extends BaseHandler<UpdateCottageRequest, UpdateCottageResponse> {
    @Inject
    private ICottageService cottageService;
    @PersistenceContext
    private EntityManager em;

    @Override
    public ArrayList<ErrorDto> validate(UpdateCottageRequest request) {
        Subject currentUser = SecurityUtils.getSubject();

        ArrayList<ErrorDto> errors = Validator.checkAllNotNull(request);

        if (!errors.isEmpty()) {
            return errors;
        }

        if (!currentUser.isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
        }

        if (!currentUser.hasRole("administrator")) {
            errors.add(new ErrorDto("Insufficient permissions.", ErrorCodes.AUTHENTICATION_ERROR));
        }

        if (cottageService.get(request.cottage.id) == null) {
            errors.add(new ErrorDto("Cottage not found", ErrorCodes.NOT_FOUND));
        }
        if (cottageService.get(request.cottage.id).getVersion() != request.cottage.version) {
            errors.add(new ErrorDto("Someone else updated this resource before you, reload the page to get latest updates.", ErrorCodes.LOCKING_ERROR));
        }

        if (request.cottage.title == null || request.cottage.title.length() < 5) {
            errors.add(new ErrorDto("Title must be at least 5 characters long", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.cottage.image == null || request.cottage.image.length() < 1) {
            errors.add(new ErrorDto("Image url must be provided", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.cottage.beds <= 0) {
            errors.add(new ErrorDto("Bed count must be higher than zero.", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.cottage.description == null || request.cottage.description.isEmpty()) {
            errors.add(new ErrorDto("Description must be provided.", ErrorCodes.VALIDATION_ERROR));
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
            sdf.setLenient(false);
            Date date = sdf.parse(request.cottage.availableFrom);
            Date date2 = sdf.parse(request.cottage.availableTo);
        } catch (ParseException e) {
            errors.add(new ErrorDto("Incorrect date format", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.cottage.services != null) {
            List<Service> services = cottageService.getCottageServices(request.cottage.id);
            for (ExistingServiceDto dto : request.cottage.services) {
                if (dto.id != 0 && !services.stream().anyMatch(s -> s.getId() == dto.id)) {
                    errors.add(new ErrorDto("Service does not belong to this cottage.", ErrorCodes.VALIDATION_ERROR));
                }
                if (dto.maxCount <= 0) {
                    errors.add(new ErrorDto("Service max count must be at least 1.", ErrorCodes.VALIDATION_ERROR));
                }
                if (dto.description == null || dto.description.isEmpty()) {
                    errors.add(new ErrorDto("Service description must be provided.", ErrorCodes.VALIDATION_ERROR));
                }
                if (dto.price < 0) {
                    errors.add(new ErrorDto("Service price can not be negative.", ErrorCodes.VALIDATION_ERROR));
                }
            }
        }
        return errors;
    }

    @Override
    public UpdateCottageResponse handleBase(UpdateCottageRequest request) {
        UpdateCottageResponse response = createResponse();

        Cottage cottage = cottageService.get(request.cottage.id);

        cottage.setTitle(request.cottage.title);
        cottage.setBedcount(request.cottage.beds);
        cottage.setImageurl(request.cottage.image);
        cottage.setDescription(request.cottage.description);
        cottage.setPrice(request.cottage.price);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        sdf.setLenient(false);
        try {
            cottage.setAvailableFrom(sdf.parse(request.cottage.availableFrom));
            cottage.setAvailableTo(sdf.parse(request.cottage.availableTo));
        } catch (ParseException ignored) {
        }

        em.merge(cottage);
        List<Service> existingServices = cottageService.getCottageServices(request.cottage.id);
        if (request.cottage.services != null)
            for (ExistingServiceDto dto : request.cottage.services) {
                Service service = em.find(Service.class, dto.id);
                if (service == null)
                    service = new Service();
                else {
                    existingServices.remove(service);
                }
                service.setDescription(dto.description);
                service.setCottage(cottage);
                service.setMaxCount(dto.maxCount);
                service.setPrice(dto.price);
                em.merge(service);
            }
        for (Service s : existingServices) {
            em.remove(s);
        }
        em.flush();
        em.refresh(cottage);
        response.cottage = new CottageDto(cottage);

        return response;
    }

    @Override
    public UpdateCottageResponse createResponse() {
        return new UpdateCottageResponse();
    }
}
