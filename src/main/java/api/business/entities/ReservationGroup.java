package api.business.entities;

import javax.persistence.*;

@Entity
@Table(name = "Reservationgroups", schema = "main", catalog = "clubby")
public class ReservationGroup {
    private int reservationgroupid;
    private int generation;
    private int groupnumber;

    public ReservationGroup(User user, int generation, int groupnumber){
        this.generation = generation;
        this.groupnumber = groupnumber;
        this.setUser(user);
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

    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userid", referencedColumnName = "id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservationGroup that = (ReservationGroup) o;

        if (reservationgroupid !=  that.reservationgroupid)
            return false;
        if (generation != that.generation) return false;
        if (groupnumber != that.groupnumber) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = reservationgroupid;
        result = 31 * result + generation;
        result = 31 * result + groupnumber;
        return result;
    }
}
