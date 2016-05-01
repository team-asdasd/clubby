package api.business.entities;

import javax.persistence.*;

/**
 * Created by Mindaugas on 30/04/2016.
 */

@Entity
@Table(name = "moneytransactions", schema = "payment", catalog = "clubby")
public class MoneyTransaction {
    private String transactionid;
    private int transactiontypeid;
    private int status;
    private int ammount;
    private int ammountclubby;

    @Id
    @Column(name = "transactionid")
    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    @Basic
    @Column(name = "transactiontypeid")
    public int getTransactiontypeid() {
        return transactiontypeid;
    }

    public void setTransactiontypeid(int transactiontypeid) {
        this.transactiontypeid = transactiontypeid;
    }

    @Basic
    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Basic
    @Column(name = "ammount")
    public int getAmmount() {
        return ammount;
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }

    @Basic
    @Column(name = "ammountclubby")
    public int getAmmountclubby() {
        return ammountclubby;
    }

    public void setAmmountclubby(int ammountclubby) {
        this.ammountclubby = ammountclubby;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoneyTransaction that = (MoneyTransaction) o;

        if (transactionid != null ? !transactionid.equals(that.transactionid) : that.transactionid != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = transactionid != null ? transactionid.hashCode() : 0;
        return result;
    }

    private Payment payment;

    @OneToOne
    @JoinColumn(name = "paymentid", referencedColumnName = "paymentid")
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userid", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
