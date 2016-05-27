package api.business.entities;

import javax.persistence.*;

@Entity
@Table(name = "Reservationgroups", schema = "main", catalog = "clubby")
public class ReservationGroup {
    private int reservationgroupid;
    private int userid;
    private int generation;
    private int groupnumber;

    public ReservationGroup(int userId, int generation, int groupnumber){
        this.userid = userId;
        this.generation = generation;
        this.groupnumber = groupnumber;
    }
    public ReservationGroup(){}

    @Id
    @Column(name = "reservationgroupid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getReservationgroupid() {
        return reservationgroupid;
    }

    public void setReservationgroupid(int reservationgroupid) {
        this.reservationgroupid = reservationgroupid;
    }

    @Basic
    @Column(name = "userid")
    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Basic
    @Column(name = "generation")
    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    @Basic
    @Column(name = "groupnumber")
    public int getGroupnumber() {
        return groupnumber;
    }

    public void setGroupnumber(int groupnumber) {
        this.groupnumber = groupnumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationGroup that = (ReservationGroup) o;

        if (reservationgroupid !=  that.reservationgroupid)
            return false;
        if (userid != that.userid) return false;
        if (generation != that.generation) return false;
        if (groupnumber != that.groupnumber) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = reservationgroupid;
        result = 31 * result + userid;
        result = 31 * result + generation;
        result = 31 * result + groupnumber;
        return result;
    }
}
