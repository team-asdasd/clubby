package api.handlers.form;

import api.business.entities.Field;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.IFormService;
import api.contracts.base.ErrorDto;
import api.contracts.form.GetFormRequest;
import api.contracts.form.GetFormResponse;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
import org.apache.shiro.SecurityUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;

@Stateless
public class GetFormHandler extends BaseHandler<GetFormRequest, GetFormResponse> {
    @Inject
    IFormService formService;
    @Inject
    ISimpleEntityManager simpleEntityManager;

    @Override
    public ArrayList<ErrorDto> validate(GetFormRequest request) {
        return new Validator().allFieldsSet(request).getErrors();
    }

    @Override
    public GetFormResponse handleBase(GetFormRequest request) {
        GetFormResponse response = createResponse();
        if (SecurityUtils.getSubject().hasRole("administrator")) {
            response.fields = simpleEntityManager.getAll(Field.class);
        } else {
            response.fields = formService.getVisibleFields();
        }
        Collections.sort(response.fields, (a, b) -> Integer.compare(b.getId(), a.getId()));
        return response;
    }

    @Override
    public GetFormResponse createResponse() {
        return new GetFormResponse();
    }
}
