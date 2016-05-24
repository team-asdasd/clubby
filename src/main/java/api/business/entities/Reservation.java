package api.business.entities;

import javax.persistence.*;

@Entity
@Table(name = "reservations", schema = "main", catalog = "clubby")
public class Reservation {
    private int reservationid;
    private int status;
    private Cottage cottage;
    private User user;
    private Payment payment;

    @Id
    @Column(name = "reservationid", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getReservationid() {
        return reservationid;
    }

    public void setReservationid(int reservationid) {
        this.reservationid = reservationid;
    }

    @Basic
    @Column(name = "status", nullable = true)
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;

        if (reservationid != that.reservationid) return false;
        if (status != that.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = reservationid;
        result = 31 * result + status;
        return result;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "userid", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "cottageid", referencedColumnName = "id")
    public Cottage getCottage() {
        return cottage;
    }

    public void setCottage(Cottage cottage) {
        this.cottage = cottage;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "paymentid", referencedColumnName = "paymentid")
    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
