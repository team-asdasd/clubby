package api.handlers.settings;

import api.business.entities.Configuration;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.dto.UpdateSettingsDto;
import api.contracts.settings.UpdateSettingsRequest;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;

@Stateless
public class UpdateSettingsHandler extends BaseHandler<UpdateSettingsRequest, BaseResponse> {

    @PersistenceContext
    private EntityManager em;

    @Override
    public ArrayList<ErrorDto> validate(UpdateSettingsRequest request) {
        ArrayList<ErrorDto> errors = new ArrayList<>();

        if (!SecurityUtils.getSubject().isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.AUTHENTICATION_ERROR));
            return errors;
        }
        if (!SecurityUtils.getSubject().hasRole("administrator")) {
            errors.add(new ErrorDto("Permission denied", ErrorCodes.AUTHENTICATION_ERROR));
            return errors;
        }
        for (UpdateSettingsDto dto : request.settings) {
            Configuration c = em.find(Configuration.class, dto.key);
            if (c == null) {
                errors.add(new ErrorDto("setting " + dto.key + " not found", ErrorCodes.NOT_FOUND));
            }
        }
        return errors;
    }

    @Override
    public BaseResponse handleBase(UpdateSettingsRequest request) {
        for (UpdateSettingsDto dto : request.settings) {
            Configuration c = em.find(Configuration.class, dto.key);
            c.setValue(dto.value);
        }
        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
