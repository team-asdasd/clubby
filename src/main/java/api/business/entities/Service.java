package api.business.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "services", schema = "main", catalog = "clubby")
public class Service {
    private int id;
    private int price;
    private String description;
    private Cottage cottage;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "price")
    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToOne
    public Cottage getCottage() {
        return cottage;
    }
    public void setCottage(Cottage cottage) {
        this.cottage = cottage;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Service service = (Service) o;

        if (id != service.id) return false;
        if (price != service.price) return false;
        return description != null ? description.equals(service.description) : service.description == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + price;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
