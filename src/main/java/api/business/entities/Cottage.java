package api.business.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "cottages", schema = "main", catalog = "clubby")
public class Cottage {

    private int price;
    private String description;
    private int id;
    private String title;
    private int bedcount;
    private String imageurl;
    private int version;
    private Date availablefrom;
    private Date availableTo;
    private List<Service> services;
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

    @Basic
    @Column(name = "title", nullable = true, length = -1)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "bedcount", nullable = true)
    public int getBedcount() {
        return bedcount;
    }

    public void setBedcount(int bedcount) {
        this.bedcount = bedcount;
    }

    @Basic
    @Column(name = "imageurl", nullable = true, length = -1)
    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    @Version
    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cottage cottage = (Cottage) o;

        if (price != cottage.price) return false;
        if (id != cottage.id) return false;
        if (bedcount != cottage.bedcount) return false;
        if (version != cottage.version) return false;
        if (description != null ? !description.equals(cottage.description) : cottage.description != null) return false;
        if (title != null ? !title.equals(cottage.title) : cottage.title != null) return false;
        if (imageurl != null ? !imageurl.equals(cottage.imageurl) : cottage.imageurl != null) return false;
        if (availablefrom != null ? !availablefrom.equals(cottage.availablefrom) : cottage.availablefrom != null)
            return false;
        if (availableTo != null ? !availableTo.equals(cottage.availableTo) : cottage.availableTo != null) return false;
        return services != null ? services.equals(cottage.services) : cottage.services == null;

    }

    @Override
    public int hashCode() {
        int result = price;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + bedcount;
        result = 31 * result + (imageurl != null ? imageurl.hashCode() : 0);
        result = 31 * result + version;
        result = 31 * result + (availablefrom != null ? availablefrom.hashCode() : 0);
        result = 31 * result + (availableTo != null ? availableTo.hashCode() : 0);
        result = 31 * result + (services != null ? services.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "availablefrom")
    public Date getAvailableFrom() {
        return availablefrom;
    }

    public void setAvailableFrom(Date availableFrom) {
        this.availablefrom = availableFrom;
    }

    @Basic
    @Column(name = "availableto")
    public Date getAvailableTo() {
        return availableTo;
    }

    public void setAvailableTo(Date availableTo) {
        this.availableTo = availableTo;
    }

    @OneToMany(mappedBy = "cottage")
    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

}
