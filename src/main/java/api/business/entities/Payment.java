package api.business.entities;

import api.contracts.enums.*;
import api.contracts.enums.TransactionStatus;
import api.helpers.DatesHelper;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.Months;

import javax.persistence.*;
import java.time.Month;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "payments", schema = "payment", catalog = "clubby")
public class Payment {
    private int paymentid;
    private int paymenttypeid;
    private String currency;
    private String paytext;
    private boolean active;
    private boolean required;
    private int frequencyId;

    @Id
    @Column(name = "paymentid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name = "frequencyId")
    public int getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(int frequencyId) {
        this.frequencyId = frequencyId;
    }

    @Basic
    @Column(name = "required")
    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Basic
    @Column(name = "currency")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

        if (paymentid != payment.paymentid)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = paymentid;

        return result;
    }

    private PaymentsSettings settings;

    @ManyToOne(optional = true)
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

    private Collection<PendingPayment> pendingPayments;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.PERSIST)
    public Collection<PendingPayment> getPendingPayments() {
        return pendingPayments;
    }

    public void setPendingPayments(Collection<PendingPayment> pendingPayments) {
        this.pendingPayments = pendingPayments;
    }

    private Collection<LineItem> lineItems;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.PERSIST)
    public Collection<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(Collection<LineItem> lineitems) {
        this.lineItems = lineitems;
    }

    @Transient
    public int calculatePrice() {
        float price = 0;

        for (LineItem item : lineItems) {
            price += item.getPrice() * item.getQuantity();
        }

        return (int) price;
    }

    @Transient
    public boolean canAcces(User user) {
        boolean canAcces = getPendingPayments().size() == 0 ||
                getPendingPayments().stream().filter(p -> p.getUserId() == user.getId()).findFirst().isPresent();

        if(canAcces){
            if(getFrequencyId() == PaymentsFrequency.once.getValue()){
                canAcces = !user.getTransactions().stream().filter(f ->
                        f.getStatus() == TransactionStatus.approved.getValue()
                            && f.getPayment().getPaymentid() == paymentid).findAny().isPresent();
            }else if(getFrequencyId() == PaymentsFrequency.monthly.getValue()){
               canAcces = !user.getTransactions().stream()
                        .filter(f ->
                                f.getStatus() == TransactionStatus.approved.getValue()
                                        && DatesHelper.inThisMonth(f.getCreationTime())
                                        && f.getPayment().getPaymentid() == paymentid).findAny().isPresent();
           }else if(getFrequencyId() == PaymentsFrequency.yearly.getValue()){
                canAcces = !user.getTransactions().stream()
                        .filter(f ->
                                f.getStatus() == TransactionStatus.approved.getValue()
                                        && DatesHelper.inThisYear(f.getCreationTime())
                                        && f.getPayment().getPaymentid() == paymentid).findAny().isPresent();
            }
        }

        return canAcces;
    }
}
