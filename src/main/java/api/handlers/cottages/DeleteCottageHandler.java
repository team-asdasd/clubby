package api.handlers.cottages;

import api.business.services.interfaces.ICottageService;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.cottages.DeleteCottageRequest;
import api.handlers.base.BaseHandler;
import api.helpers.Validator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class DeleteCottageHandler extends BaseHandler<DeleteCottageRequest, BaseResponse> {
    @Inject
    private ICottageService cottageService;

    @Override
    public ArrayList<ErrorDto> validate(DeleteCottageRequest request) {
        ArrayList<ErrorDto> errors = Validator.checkAllNotNull(request);

        if (cottageService.getCottage(request.Id) == null) {
            errors.add(new ErrorDto("Cottage not found", ErrorCodes.NOT_FOUND));
        }

        return errors;
    }

    @Override
    public BaseResponse handleBase(DeleteCottageRequest request) {
        cottageService.deleteCottage(request.Id);

        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
