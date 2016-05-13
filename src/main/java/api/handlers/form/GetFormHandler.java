package api.handlers.form;

import api.contracts.base.ErrorDto;
import api.contracts.form.GetFormRequest;
import api.contracts.form.GetFormResponse;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;
import web.helpers.FormStateHelper;

import javax.inject.Inject;
import java.util.ArrayList;

public class GetFormHandler extends BaseHandler<GetFormRequest, GetFormResponse> {
    @Inject
    private FormStateHelper formStateHelper;
    @Override
    public ArrayList<ErrorDto> validate(GetFormRequest request) {
        return Validator.checkAllNotNullAndIsAuthenticated(request);
    }

    @Override
    public GetFormResponse handleBase(GetFormRequest request) {
        return new GetFormResponse(formStateHelper.getFormState());
    }

    @Override
    public GetFormResponse createResponse() {
        return new GetFormResponse();
    }
}
