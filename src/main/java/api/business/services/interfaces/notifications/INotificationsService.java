package api.business.services.interfaces.notifications;

import api.business.entities.notifications.NotificationView;

import java.util.List;

public interface INotificationsService {
    List<NotificationView> getAll(int userId);

    void markAsRead(int notifcationId, int userId);

    void create(String title, String action);
}
