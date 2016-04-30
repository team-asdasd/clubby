package api.business.entities;

import javax.persistence.*;

/**
 * Created by Mindaugas on 30/04/2016.
 */

@Entity
@Table(name = "moneytransactions", schema = "payment", catalog = "clubby")
public class MoneyTransaction {
    private String transactionid;
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

    private TransactionStatus transactionStatus;

    @ManyToOne(optional = false)
    @JoinColumn(name = "status", referencedColumnName = "status")
    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    private TransactionType transactionType;

    @ManyToOne(optional = false)
    @JoinColumn(name = "transactiontypeid", referencedColumnName = "transactiontypeid")
    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
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
