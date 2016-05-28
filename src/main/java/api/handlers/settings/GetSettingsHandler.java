package api.handlers.settings;

import api.business.entities.Configuration;
import api.business.persistance.ISimpleEntityManager;
import api.contracts.base.BaseRequest;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.settings.GetSettingsResponse;
import api.handlers.base.BaseHandler;
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
        ArrayList<ErrorDto> errors = new ArrayList<>();
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            errors.add(new ErrorDto("Not authenticated.", ErrorCodes.UNAUTHENTICATED));
            return errors;
        }
        if (!SecurityUtils.getSubject().hasRole("administrator")) {
            errors.add(new ErrorDto("Permission denied", ErrorCodes.UNAUTHENTICATED));
            return errors;
        }


        return errors;
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
