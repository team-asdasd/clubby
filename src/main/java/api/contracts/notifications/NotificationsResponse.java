package api.contracts.notifications;

import api.contracts.base.BaseResponse;
import api.contracts.dto.notifications.NotificationDto;

import java.util.List;

public class NotificationsResponse extends BaseResponse {
    public List<NotificationDto> notifications;
}
