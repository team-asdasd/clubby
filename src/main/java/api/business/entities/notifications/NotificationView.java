package api.business.entities.notifications;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "notifications_user", schema = "notification", catalog = "clubby")
public class NotificationView implements Serializable {
    private int userId;
    private boolean isRead;
    private Notification notification;
    private String argument;

    public NotificationView() {
    }

    public NotificationView(int userId, boolean isRead, Notification notification, String argument) {
        this.userId = userId;
        this.isRead = isRead;
        this.notification = notification;
        this.argument = argument;
    }

    @Id
    @Column(name = "user_id", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "is_read", nullable = true)
    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    @Basic
    @Column(name = "argument", nullable = true)
    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.argument = argument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NotificationView that = (NotificationView) o;

        if (userId != that.userId) return false;
        if (isRead != that.isRead) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = 31 * userId;
        result = 31 * result + (isRead ? 1 : 0);
        return result;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "notification_id", referencedColumnName = "id", nullable = false)
    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notificationsByNotificationId) {
        this.notification = notificationsByNotificationId;
    }
}
