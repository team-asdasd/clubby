package api.business.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "logins", schema = "security", catalog = "clubby")
public class Login implements Serializable{
    private int id;
    private String email;
    private String password;
    private User user;
    private List<Role> roles;
    private String facebookId;
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
    @Column(name = "username", length = -1)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password", nullable = true, length = -1)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Login login = (Login) o;

        if (id != login.id) return false;
        if (email != null ? !email.equals(login.email) : login.email != null) return false;
        if (password != null ? !password.equals(login.password) : login.password != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }

    @OneToMany
    @JoinColumn(name="username", referencedColumnName = "username")
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> role) {
        this.roles = role;
    }

    @Basic
    @Column(name = "facebook_id", nullable = true, length = -1)
    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String id) {
        this.facebookId = id;
    }

    @Transient
    public boolean isFacebookUser() {
        return facebookId != null && !facebookId.isEmpty();
    }
}
