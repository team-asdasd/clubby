package api.handlers.notifications;

import api.business.entities.User;
import api.business.services.interfaces.IUserService;
import api.business.services.interfaces.notifications.INotificationsService;
import api.contracts.base.BaseResponse;
import api.contracts.base.ErrorDto;
import api.contracts.notifications.ReadNotificationsRequest;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;

import javax.inject.Inject;
import java.util.ArrayList;

public class ReadNotificationHandler extends BaseHandler<ReadNotificationsRequest, BaseResponse>{
    @Inject
    private INotificationsService notificationsService;
    @Inject
    private IUserService userService;

    @Override
    public ArrayList<ErrorDto> validate(ReadNotificationsRequest request) {
        return new Validator().allFieldsSet(request).isAuthenticated().getErrors();
    }

    @Override
    public BaseResponse handleBase(ReadNotificationsRequest request) {
        User user = userService.get();
        for (int id : request.notifications){
            notificationsService.markAsRead(id, user.getId());
        }
        return createResponse();
    }

    @Override
    public BaseResponse createResponse() {
        return new BaseResponse();
    }
}
