package api.handlers.settings;

import api.business.entities.Configuration;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.dto.UpdateSettingsDto;
import api.contracts.settings.UpdateSettingsRequest;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
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

        errors.addAll(new Validator().isAdministrator().getErrors());
        if (!errors.isEmpty()) return errors;

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
