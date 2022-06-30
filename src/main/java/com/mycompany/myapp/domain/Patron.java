package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Category;
import com.mycompany.myapp.domain.enumeration.PatronType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Patron.
 */
@Entity
@Table(name = "patron")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Patron implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "ref")
    private String ref;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PatronType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "size_min")
    private Integer sizeMin;

    @Column(name = "size_max")
    private Integer sizeMax;

    @Column(name = "buy_date")
    private LocalDate buyDate;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @ManyToOne
    private PatronEditor from;

    @OneToMany(mappedBy = "patron")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "patron", "matieres" }, allowSetters = true)
    private Set<Project> projetcs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Patron id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Patron name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return this.ref;
    }

    public Patron ref(String ref) {
        this.setRef(ref);
        return this;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public PatronType getType() {
        return this.type;
    }

    public Patron type(PatronType type) {
        this.setType(type);
        return this;
    }

    public void setType(PatronType type) {
        this.type = type;
    }

    public Category getCategory() {
        return this.category;
    }

    public Patron category(Category category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getSizeMin() {
        return this.sizeMin;
    }

    public Patron sizeMin(Integer sizeMin) {
        this.setSizeMin(sizeMin);
        return this;
    }

    public void setSizeMin(Integer sizeMin) {
        this.sizeMin = sizeMin;
    }

    public Integer getSizeMax() {
        return this.sizeMax;
    }

    public Patron sizeMax(Integer sizeMax) {
        this.setSizeMax(sizeMax);
        return this;
    }

    public void setSizeMax(Integer sizeMax) {
        this.sizeMax = sizeMax;
    }

    public LocalDate getBuyDate() {
        return this.buyDate;
    }

    public Patron buyDate(LocalDate buyDate) {
        this.setBuyDate(buyDate);
        return this;
    }

    public void setBuyDate(LocalDate buyDate) {
        this.buyDate = buyDate;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Patron image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Patron imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public PatronEditor getFrom() {
        return this.from;
    }

    public void setFrom(PatronEditor patronEditor) {
        this.from = patronEditor;
    }

    public Patron from(PatronEditor patronEditor) {
        this.setFrom(patronEditor);
        return this;
    }

    public Set<Project> getProjetcs() {
        return this.projetcs;
    }

    public void setProjetcs(Set<Project> projects) {
        if (this.projetcs != null) {
            this.projetcs.forEach(i -> i.setPatron(null));
        }
        if (projects != null) {
            projects.forEach(i -> i.setPatron(this));
        }
        this.projetcs = projects;
    }

    public Patron projetcs(Set<Project> projects) {
        this.setProjetcs(projects);
        return this;
    }

    public Patron addProjetcs(Project project) {
        this.projetcs.add(project);
        project.setPatron(this);
        return this;
    }

    public Patron removeProjetcs(Project project) {
        this.projetcs.remove(project);
        project.setPatron(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patron)) {
            return false;
        }
        return id != null && id.equals(((Patron) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patron{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", ref='" + getRef() + "'" +
            ", type='" + getType() + "'" +
            ", category='" + getCategory() + "'" +
            ", sizeMin=" + getSizeMin() +
            ", sizeMax=" + getSizeMax() +
            ", buyDate='" + getBuyDate() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
