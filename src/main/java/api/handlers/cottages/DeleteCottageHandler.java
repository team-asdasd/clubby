package api.handlers.cottages;

import api.business.services.interfaces.ICottageService;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.cottages.DeleteCottageRequest;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class DeleteCottageHandler extends BaseHandler<DeleteCottageRequest, BaseResponse> {
    @Inject
    private ICottageService cottageService;

    @Override
    public ArrayList<ErrorDto> validate(DeleteCottageRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAuthenticated().getErrors();

        if (!authErrors.isEmpty()) return authErrors;

        ArrayList<ErrorDto> errors = new Validator().isAdministrator().allFieldsSet(request).getErrors();

        if (cottageService.get(request.id) == null) {
            errors.add(new ErrorDto("Cottage does not exist.", ErrorCodes.NOT_FOUND));
        }

        return errors;
    }

    @Override
    public BaseResponse handleBase(DeleteCottageRequest request) {
        cottageService.delete(request.id);

        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
