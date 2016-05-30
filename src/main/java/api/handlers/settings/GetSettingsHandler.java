package api.handlers.settings;

import api.business.entities.Configuration;
import api.business.persistance.ISimpleEntityManager;
import api.contracts.base.BaseRequest;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.settings.GetSettingsResponse;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
import org.apache.shiro.SecurityUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class GetSettingsHandler extends BaseHandler<BaseRequest, GetSettingsResponse> {
    @Inject
    private ISimpleEntityManager em;

    @Override
    public ArrayList<ErrorDto> validate(BaseRequest request) {

        return new Validator().isAdministrator().getErrors();

    }

    @Override
    public GetSettingsResponse handleBase(BaseRequest request) {
        GetSettingsResponse response = createResponse();
        response.settings = em.getAll(Configuration.class);
        return response;
    }

    @Override
    public GetSettingsResponse createResponse() {
        return new GetSettingsResponse();
    }
}
