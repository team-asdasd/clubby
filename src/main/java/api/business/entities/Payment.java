package api.business.entities;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Mindaugas on 30/04/2016.
 */

@Entity
@Table(name = "payments", schema = "payment", catalog = "clubby")
public class Payment {
    private int paymentid;
    private int paymenttypeid;
    private int amount;
    private String paytext;
    private boolean active;

    @Id
    @Column(name = "paymentid")
    public int getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(int paymentid) {
        this.paymentid = paymentid;
    }

    @Basic
    @Column(name = "paymenttypeid")
    public int getPaymenttypeid() {
        return paymenttypeid;
    }

    public void setPaymenttypeid(int paymenttypeid) {
        this.paymenttypeid = paymenttypeid;
    }

    @Basic
    @Column(name = "amount")
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "paytext")
    public String getPaytext() {
        return paytext;
    }

    public void setPaytext(String paytext) {
        this.paytext = paytext;
    }

    @Basic
    @Column(name = "active")
    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payment payment = (Payment) o;

        if (paymentid  != payment.paymentid)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = paymentid;

        return result;
    }

    private PaymentsSettings settings;

    @ManyToOne(optional = false)
    @JoinColumn(name = "paymentSettingsId", referencedColumnName = "paymentSettingsId")
    public PaymentsSettings getSettings() {
        return settings;
    }

    public void setSettings(PaymentsSettings settings) {
        this.settings = settings;
    }

    private Collection<MoneyTransaction> transactions;

    @OneToMany(mappedBy = "payment")
    public Collection<MoneyTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection<MoneyTransaction> transactions) {
        this.transactions = transactions;
    }
}
