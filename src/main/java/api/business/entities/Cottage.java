package api.business.entities;

import javax.persistence.*;

@Entity
@Table(name = "cottages", schema = "main", catalog = "clubby")
public class Cottage {
    private int id;
    private String title;
    private int bedcount;
    private String imageurl;
    private int version;

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

        if (id != cottage.id) return false;
        if (bedcount != cottage.bedcount) return false;
        if (title != null ? !title.equals(cottage.title) : cottage.title != null) return false;
        if (imageurl != null ? !imageurl.equals(cottage.imageurl) : cottage.imageurl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + bedcount;
        result = 31 * result + (imageurl != null ? imageurl.hashCode() : 0);
        return result;
    }
}
