package api.handlers.notifications;

import api.business.entities.notifications.NotificationView;
import api.business.services.interfaces.IUserService;
import api.business.services.interfaces.notifications.INotificationsService;
import api.contracts.base.BaseRequest;
import api.contracts.base.ErrorDto;
import api.contracts.dto.notifications.NotificationDto;
import api.contracts.notifications.NotificationsResponse;
import api.handlers.base.BaseHandler;
import api.helpers.validator.Validator;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
public class GetNotificationsHandler extends BaseHandler<BaseRequest, NotificationsResponse> {
    @Inject
    private INotificationsService notificationsService;
    @Inject
    private IUserService userService;

    @Override
    public ArrayList<ErrorDto> validate(BaseRequest request) {
        return new Validator().isAuthenticated().getErrors();
    }

    @Override
    public NotificationsResponse handleBase(BaseRequest request) {
        NotificationsResponse response = createResponse();

        int id = userService.get().getId();
        List<NotificationView> notifications = notificationsService.getAllUnread(id);
        if(notifications.size() < 5){
            notifications.addAll(notificationsService.getLastRead(id, 5 - notifications.size()));
        }
        notifications = notifications.stream().sorted((n1, n2) -> Integer.compare(n2.getNotification().getId(), n1.getNotification().getId())).collect(Collectors.toList());
        response.notifications = notifications.stream().map(NotificationDto::new).collect(Collectors.toList());

        return response;
    }

    @Override
    public NotificationsResponse createResponse() {
        return new NotificationsResponse();
    }
}
