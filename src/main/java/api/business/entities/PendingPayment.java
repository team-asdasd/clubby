package api.business.entities;

import javax.persistence.*;

@Entity
@Table(name = "pendingPayments", schema = "payment", catalog = "clubby")
public class PendingPayment {
    private int pendingPaymentId;
    private int userId;

    @Id
    @Column(name = "pendingPaymentId")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getPendingPaymentId() {
        return pendingPaymentId;
    }

    public void setPendingPaymentId(int pendingPaymentId) {
        this.pendingPaymentId = pendingPaymentId;
    }

    @Basic
    @Column(name = "userId")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "paymentId", referencedColumnName = "paymentId")
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PendingPayment that = (PendingPayment) o;

        if (pendingPaymentId != that.pendingPaymentId)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = pendingPaymentId;
        result = 31 * result + userId;

        return result;
    }
}
