package api.handlers.payments;

import api.business.services.interfaces.IPaymentsService;
import api.business.services.interfaces.IUserService;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.payments.GiftPointsRequest;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class GiftPointsHandler extends BaseHandler<GiftPointsRequest, BaseResponse> {
    @Inject
    private IUserService userService;
    @Inject
    private IPaymentsService paymentsService;

    @Override
    public ArrayList<ErrorDto> validate(GiftPointsRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAuthenticated().getErrors();
        if (!authErrors.isEmpty()) return authErrors;


        ArrayList<ErrorDto> errors = new Validator().isAdministrator().allFieldsSet(request).isValidId(request.user).getErrors();

        if (!errors.isEmpty()) return errors;

        if (userService.get(request.user) == null) {
            errors.add(new ErrorDto("User does not exist.", ErrorCodes.VALIDATION_ERROR));
        }

        if (request.amount <= 0) {
            errors.add(new ErrorDto("Amount must be a positive number.", ErrorCodes.VALIDATION_ERROR));
        }

        return errors;
    }

    @Override
    public BaseResponse handleBase(GiftPointsRequest request) {
        paymentsService.createGift(request.user, request.reason, request.amount);

        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
