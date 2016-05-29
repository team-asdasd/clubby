package api.business.services.notifications;

import api.business.entities.User;
import api.business.entities.notifications.Notification;
import api.business.entities.notifications.NotificationView;
import api.business.persistance.ISimpleEntityManager;
import api.business.services.interfaces.notifications.INotificationsService;
import api.contracts.enums.NotificationAction;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public class NotificationsService implements INotificationsService {
    @PersistenceContext
    private EntityManager em;

    @Inject
    private ISimpleEntityManager sem;

    public List<NotificationView> getAllUnread(int userId) {
        return em.createQuery("SELECT NV FROM NotificationView NV WHERE NV.userId = :userId AND NV.read = FALSE", NotificationView.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<NotificationView> getLastRead(int userId, int count) {

        return em.createQuery("SELECT NV FROM NotificationView NV WHERE NV.userId = :userId AND NV.read = TRUE ORDER BY NV.notification.id DESC", NotificationView.class)
                .setParameter("userId", userId)
                .setMaxResults(count)
                .getResultList();
    }

    public void markAsRead(int notificationId, int userId) {
        em.createQuery("UPDATE NotificationView AS NV SET NV.read = TRUE WHERE NV.notification.id = :notificationId AND NV.userId = :userId")
                .setParameter("notificationId", notificationId)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    public void create(String title, String action) {
        Notification notification = new Notification(title, action);
        em.persist(notification);
        sem.getAll(User.class).stream().forEach(u -> em.persist(new NotificationView(u.getId(), false, notification)));
    }

    public void create(String title, NotificationAction action, int userId) {
        Notification notification = new Notification(title, action.name());
        em.persist(notification);

        em.persist(new NotificationView(userId, false, notification));
    }
}
