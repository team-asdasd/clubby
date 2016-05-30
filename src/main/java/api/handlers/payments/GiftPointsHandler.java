package api.handlers.payments;

import api.business.services.interfaces.IPaymentsService;
import api.business.services.interfaces.IUserService;
import api.business.services.interfaces.notifications.INotificationsService;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorCodes;
import api.contracts.base.ErrorDto;
import api.contracts.enums.NotificationAction;
import api.contracts.payments.GiftPointsRequest;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;
import logging.audit.Audit;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class GiftPointsHandler extends BaseHandler<GiftPointsRequest, BaseResponse> {
    @Inject
    private IUserService userService;
    @Inject
    private IPaymentsService paymentsService;
    @Inject
    private INotificationsService notificationsService;
    private final String giftReceivedNotification = "Gift received.";

    @Override
    public ArrayList<ErrorDto> validate(GiftPointsRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAdministrator().getErrors();
        if (!authErrors.isEmpty()) return authErrors;


        ArrayList<ErrorDto> errors = new Validator().allFieldsSet(request).isValidId(request.user).getErrors();

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
    @Audit
    public BaseResponse handleBase(GiftPointsRequest request) {
        paymentsService.createGift(request.user, request.reason, request.amount);
        notificationsService.create(giftReceivedNotification, NotificationAction.PAYMENTS, request.user, null);
        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
