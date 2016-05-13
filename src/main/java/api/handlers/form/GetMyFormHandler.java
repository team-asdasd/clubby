package api.handlers.form;

import api.business.services.interfaces.IFormService;
import api.contracts.base.BaseRequest;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.form.GetMyFormResponse;
import api.handlers.base.BaseHandler;
import org.apache.shiro.SecurityUtils;

import javax.inject.Inject;
import java.util.ArrayList;

public class GetMyFormHandler extends BaseHandler<BaseRequest, GetMyFormResponse> {
    @Inject
    private IFormService formService;

    @Override
    public ArrayList<ErrorDto> validate(BaseRequest request) {
        ArrayList<ErrorDto> errors = new ArrayList<>();
        if(!SecurityUtils.getSubject().isAuthenticated()){
            errors.add(new ErrorDto("Not authenticated", ErrorCodes.AUTHENTICATION_ERROR));
        }
        return errors;
    }

    @Override
    public GetMyFormResponse handleBase(BaseRequest request) {
        GetMyFormResponse response = new GetMyFormResponse();

        response.fields = formService.getMyFields();
        return response;
    }

    @Override
    public GetMyFormResponse createResponse() {
        return new GetMyFormResponse();
    }
}
