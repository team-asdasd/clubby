package api.handlers.form;

import api.business.entities.Field;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IFormService;
import api.contracts.base.ErrorDto;
import api.contracts.form.GetFormRequest;
import api.contracts.form.GetFormResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import web.helpers.FormStateHelper;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class GetFormHandler extends BaseHandler<GetFormRequest, GetFormResponse> {
    @Inject
    IFormService formService;
    @Inject
    ISimpleEntityManager simpleEntityManager;
    @Override
    public ArrayList<ErrorDto> validate(GetFormRequest request) {
        return Validator.checkAllNotNullAndIsAuthenticated(request);
    }

    @Override
    public GetFormResponse handleBase(GetFormRequest request) {
        GetFormResponse response = createResponse();
        response.fields = simpleEntityManager.getAll(Field.class);
        return response;
    }

    @Override
    public GetFormResponse createResponse() {
        return new GetFormResponse();
    }
}
