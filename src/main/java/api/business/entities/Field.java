package api.business.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "form_fields", schema = "main", catalog = "clubby")
public class Field {
    private int id;
    private String name;
    private String type;
    private String validationRegex;
    private boolean required;
    private boolean visible;
    private String description;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Basic
    @Column(name = "validation_regex")
    public String getValidationRegex() {
        return validationRegex;
    }

    public void setValidationRegex(String validationRegex) {
        this.validationRegex = validationRegex;
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
    @Column(name = "visible")
    public boolean getVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (id != field.id) return false;
        if (required != field.required) return false;
        if (visible != field.visible) return false;
        if (name != null ? !name.equals(field.name) : field.name != null) return false;
        if (type != null ? !type.equals(field.type) : field.type != null) return false;
        if (validationRegex != null ? !validationRegex.equals(field.validationRegex) : field.validationRegex != null)
            return false;
        return description != null ? description.equals(field.description) : field.description == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (validationRegex != null ? validationRegex.hashCode() : 0);
        result = 31 * result + (required ? 1 : 0);
        result = 31 * result + (visible ? 1 : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
