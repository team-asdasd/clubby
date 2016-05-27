package api.business.entities;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Reservationsperiods", schema = "main", catalog = "clubby")
public class ReservationsPeriod {
    private int reservationsperiodid;
    private Date fromdate;
    private Date todate;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservationsperiodid")
    public int getReservationsperiodid() {
        return reservationsperiodid;
    }

    public void setReservationsperiodid(int reservationsperiodid) {
        this.reservationsperiodid = reservationsperiodid;
    }

    @Basic
    @Column(name = "fromdate")
    public Date getFromdate() {
        return fromdate;
    }

    public void setFromdate(Date fromdate) {
        this.fromdate = fromdate;
    }

    @Basic
    @Column(name = "todate")
    public Date getTodate() {
        return todate;
    }

    public void setTodate(Date todate) {
        this.todate = todate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationsPeriod that = (ReservationsPeriod) o;

        if (reservationsperiodid != that.reservationsperiodid)
            return false;
        if (fromdate != null ? !fromdate.equals(that.fromdate) : that.fromdate != null) return false;
        if (todate != null ? !todate.equals(that.todate) : that.todate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = reservationsperiodid;
        result = 31 * result + (fromdate != null ? fromdate.hashCode() : 0);
        result = 31 * result + (todate != null ? todate.hashCode() : 0);
        return result;
    }
}
