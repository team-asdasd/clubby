package api.business.entities;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "paymentsSettings", schema = "payment", catalog = "clubby")
public class PaymentsSettings {
    private int paymentsettingsid;
    private String projectid;
    private String version;

    @Id
    @Column(name = "paymentsettingsid")
    public int getPaymentsettingsid() {
        return paymentsettingsid;
    }

    public void setPaymentsettingsid(int paymentsettingsid) {
        this.paymentsettingsid = paymentsettingsid;
    }

    @Basic
    @Column(name = "projectid")
    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    @Basic
    @Column(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentsSettings that = (PaymentsSettings) o;

        if (paymentsettingsid != that.paymentsettingsid)
            return false;
        if (projectid != null ? !projectid.equals(that.projectid) : that.projectid != null) return false;
        if (version != null ? !version.equals(that.version) : that.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = paymentsettingsid;
        result = 31 * result + (projectid != null ? projectid.hashCode() : 0);
        result = 31 * result + (version != null ? version.hashCode() : 0);
        return result;
    }

    private Collection<Payment> payments;

    @OneToMany(mappedBy = "settings")
    public Collection<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Collection<Payment> payments) {
        this.payments = payments;
    }
}
