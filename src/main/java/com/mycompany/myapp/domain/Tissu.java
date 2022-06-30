package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.TissuType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tissu.
 */
@Entity
@Table(name = "tissu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tissu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "ref")
    private String ref;

    @Column(name = "color")
    private String color;

    @Column(name = "buy_size")
    private String buySize;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TissuType type;

    @Column(name = "buy_date")
    private LocalDate buyDate;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @ManyToMany(mappedBy = "tissus")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tissus" }, allowSetters = true)
    private Set<Seller> sellers = new HashSet<>();

    @ManyToMany(mappedBy = "matieres")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patron", "matieres" }, allowSetters = true)
    private Set<Project> projects = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tissu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Tissu name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return this.ref;
    }

    public Tissu ref(String ref) {
        this.setRef(ref);
        return this;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getColor() {
        return this.color;
    }

    public Tissu color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBuySize() {
        return this.buySize;
    }

    public Tissu buySize(String buySize) {
        this.setBuySize(buySize);
        return this;
    }

    public void setBuySize(String buySize) {
        this.buySize = buySize;
    }

    public TissuType getType() {
        return this.type;
    }

    public Tissu type(TissuType type) {
        this.setType(type);
        return this;
    }

    public void setType(TissuType type) {
        this.type = type;
    }

    public LocalDate getBuyDate() {
        return this.buyDate;
    }

    public Tissu buyDate(LocalDate buyDate) {
        this.setBuyDate(buyDate);
        return this;
    }

    public void setBuyDate(LocalDate buyDate) {
        this.buyDate = buyDate;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Tissu image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Tissu imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Set<Seller> getSellers() {
        return this.sellers;
    }

    public void setSellers(Set<Seller> sellers) {
        if (this.sellers != null) {
            this.sellers.forEach(i -> i.removeTissu(this));
        }
        if (sellers != null) {
            sellers.forEach(i -> i.addTissu(this));
        }
        this.sellers = sellers;
    }

    public Tissu sellers(Set<Seller> sellers) {
        this.setSellers(sellers);
        return this;
    }

    public Tissu addSellers(Seller seller) {
        this.sellers.add(seller);
        seller.getTissus().add(this);
        return this;
    }

    public Tissu removeSellers(Seller seller) {
        this.sellers.remove(seller);
        seller.getTissus().remove(this);
        return this;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public void setProjects(Set<Project> projects) {
        if (this.projects != null) {
            this.projects.forEach(i -> i.removeMatieres(this));
        }
        if (projects != null) {
            projects.forEach(i -> i.addMatieres(this));
        }
        this.projects = projects;
    }

    public Tissu projects(Set<Project> projects) {
        this.setProjects(projects);
        return this;
    }

    public Tissu addProjects(Project project) {
        this.projects.add(project);
        project.getMatieres().add(this);
        return this;
    }

    public Tissu removeProjects(Project project) {
        this.projects.remove(project);
        project.getMatieres().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tissu)) {
            return false;
        }
        return id != null && id.equals(((Tissu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tissu{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", ref='" + getRef() + "'" +
            ", color='" + getColor() + "'" +
            ", buySize='" + getBuySize() + "'" +
            ", type='" + getType() + "'" +
            ", buyDate='" + getBuyDate() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
