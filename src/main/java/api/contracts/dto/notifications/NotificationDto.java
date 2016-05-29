package api.contracts.dto.notifications;

import api.business.entities.notifications.NotificationView;
import com.fasterxml.jackson.annotation.JsonInclude;

public class NotificationDto {
    public int id;
    public String action;
    public String title;
    public boolean read;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String argument;

    public NotificationDto(NotificationView notification) {
        id = notification.getNotification().getId();
        title = notification.getNotification().getTitle();
        action = notification.getNotification().getAction();
        read = notification.isRead();
        argument = notification.getArgument();
    }
}
