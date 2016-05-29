package api.contracts.dto.notifications;

import api.business.entities.notifications.NotificationView;

public class NotificationDto {
    public int id;
    public String action;
    public String title;
    public boolean read;

    public NotificationDto(NotificationView notification) {
        id = notification.getNotification().getId();
        title = notification.getNotification().getTitle();
        action = notification.getNotification().getAction();
        read = notification.isRead();
    }
}
