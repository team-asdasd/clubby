package api.business.entities;

import javax.persistence.*;
import java.util.Collection;

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

    @OneToMany(mappedBy = "user")
    public Collection<FormResult> getFormResults() {
        return formResults;
    }

    public void setFormResults(Collection<FormResult> formResults) {
        this.formResults = formResults;
    }

    @Basic
    @Column(name = "picture", length = -1)
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

}