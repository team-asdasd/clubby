package api.business.services.interfaces.notifications;

import api.business.entities.notifications.NotificationView;
import api.contracts.enums.NotificationAction;

import java.util.List;

public interface INotificationsService {
    List<NotificationView> getAllUnread(int userId);

    List<NotificationView> getLastRead(int userId, int count);

    void markAsRead(int notifcationId, int userId);

    void create(String title, String action);

    void create(String title, NotificationAction action, int userId);

}
