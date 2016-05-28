package api.handlers.notifications;

import api.business.services.interfaces.notifications.INotificationsService;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorDto;
import api.contracts.notifications.CreateNotificationRequest;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
public class CreateNotificationHandler extends BaseHandler<CreateNotificationRequest, BaseResponse> {
    @Inject
    private INotificationsService notificationsService;

    @Override
    public ArrayList<ErrorDto> validate(CreateNotificationRequest request) {
        ArrayList<ErrorDto> authErrors = new Validator().isAuthenticated().getErrors();
        if (!authErrors.isEmpty()) return authErrors;

        return new Validator().isAdministrator().allFieldsSet(request).getErrors();
    }

    @Override
    public BaseResponse handleBase(CreateNotificationRequest request) {
        BaseResponse response = createResponse();

        notificationsService.create(request.title, request.action);

        return response;
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
