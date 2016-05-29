package api.business.entities.notifications;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "notifications", schema = "notification", catalog = "clubby")
public class Notification {
    private int id;
    private String title;
    private String action;
    private Collection<NotificationView> notificationsUsersById;

    public Notification() {
    }

    public Notification(String title, String action) {
        this.title = title;
        this.action = action;
    }

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "action", nullable = true, length = -1)
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        if (id != that.id) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (action != null ? !action.equals(that.action) : that.action != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (action != null ? action.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "notification")
    public Collection<NotificationView> getNotificationViews() {
        return notificationsUsersById;
    }

    public void setNotificationViews(Collection<NotificationView> notificationsUsersById) {
        this.notificationsUsersById = notificationsUsersById;
    }
}
