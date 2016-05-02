package api.business.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "logins_roles", schema = "security", catalog = "clubby")
public class LoginRole implements Serializable{
    private String roleName;
    private String username;

    @Id
    @Column(name = "role_name")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Id
    @Column(name = "username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginRole loginRole = (LoginRole) o;

        if (roleName != null ? !roleName.equals(loginRole.roleName) : loginRole.roleName != null) return false;
        if (username != null ? !username.equals(loginRole.username) : loginRole.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = roleName != null ? roleName.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }

}
