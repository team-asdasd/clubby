package api.business.entities;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.Subject;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "users", schema = "main", catalog = "clubby")
public class User {
    private int id;
    private String name;
    private Login login;
    private String picture;

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
    @Column(name = "name", length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Transient
    public boolean isFacebookUser() {
        return getLogin().isFacebookUser();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @OneToOne
    @JoinColumn(name = "login", referencedColumnName = "id")
    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    private Collection<MoneyTransaction> transactions;

    @OneToMany(mappedBy = "user")
    public Collection<MoneyTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<MoneyTransaction> transactions) {
        this.transactions = transactions;
    }

    private Collection<FormResult> formResults;
    private Collection<Reservation> reservations;
    private Collection<ReservationGroup> reservationGroups;

    @OneToMany(mappedBy = "user")
    public Collection<FormResult> getFormResults() {
        return formResults;
    }

    public void setFormResults(Collection<FormResult> formResults) {
        this.formResults = formResults;
    }

    @OneToMany(mappedBy = "user")
    public Collection<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Collection<Reservation> reservations) {
        this.reservations = reservations;
    }

    @OneToMany(mappedBy = "user")
    public Collection<ReservationGroup> getReservationGroups() {
        return reservationGroups;
    }

    public void setReservationGroups(Collection<ReservationGroup> reservationGroups) {
        this.reservationGroups = reservationGroups;
    }

    @Basic
    @Column(name = "picture", length = -1)
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Transient
    public Boolean isOnline() {
        DefaultSecurityManager securityManager = (DefaultSecurityManager) SecurityUtils.getSecurityManager();
        DefaultSessionManager sessionManager = (DefaultSessionManager) securityManager.getSessionManager();
        Collection<Session> activeSessions = sessionManager.getSessionDAO().getActiveSessions();
        return activeSessions.stream().anyMatch(this::matchSession);
    }

    @Transient
    public int activeGroup() {
        int group = 0;
        Optional<ReservationGroup> rg = this.getReservationGroups().stream()
                .sorted((s1, s2) -> Integer.compare(s2.getGeneration(), s1.getGeneration()))
                .findFirst();
        if (rg.isPresent()) {
            group = rg.get().getGroupnumber();
        }

        return group;
    }

    private boolean matchSession(Session s) {
        Subject subject = new Subject.Builder().sessionId(s.getId()).buildSubject();
        Object principal = subject.getPrincipal();
        if (principal != null) {
            int id = Integer.parseInt(principal.toString());
            return id == getId();
        }
        return false;
    }
}