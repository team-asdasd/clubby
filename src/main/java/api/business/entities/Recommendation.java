package api.business.entities;

import javax.persistence.*;

@Entity
@Table(name = "recommendations", schema = "main", catalog = "clubby")
public class Recommendation {
    private int id;
    private String recommendationCode;
    private int status;
    private User userFrom;
    private User userTo;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "recommendation_code")
    public String getRecommendationCode() {
        return recommendationCode;
    }

    public void setRecommendationCode(String recommendationCode) {
        this.recommendationCode = recommendationCode;
    }

    @Basic
    @Column(name = "status")
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

        Recommendation that = (Recommendation) o;

        if (id != that.id) return false;
        if (status != that.status) return false;
        if (!recommendationCode.equals(that.recommendationCode)) return false;
        if (!userFrom.equals(that.userFrom)) return false;
        return userTo.equals(that.userTo);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + recommendationCode.hashCode();
        result = 31 * result + status;
        result = 31 * result + userFrom.hashCode();
        result = 31 * result + userTo.hashCode();
        return result;
    }

    @OneToOne
    @JoinColumn(name = "user_from", referencedColumnName = "id")
    public User getUserFrom() {
        return userFrom;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    @OneToOne
    @JoinColumn(name = "user_to", referencedColumnName = "id")
    public User getUserTo() {
        return userTo;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }
}
