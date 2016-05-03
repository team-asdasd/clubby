package api.business.entities;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Mindaugas on 30/04/2016.
 */
@Entity
public class TransactionStatus {
    private int status;
    private String name;

    @Id
    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionStatus that = (TransactionStatus) o;

        if (status != that.status) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = status;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
