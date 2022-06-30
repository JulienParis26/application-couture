package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Project.
 */
@Entity
@Table(name = "project")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "ref")
    private String ref;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    @Lob
    @Column(name = "image_1")
    private byte[] image1;

    @Column(name = "image_1_content_type")
    private String image1ContentType;

    @Lob
    @Column(name = "image_2")
    private byte[] image2;

    @Column(name = "image_2_content_type")
    private String image2ContentType;

    @Lob
    @Column(name = "image_3")
    private byte[] image3;

    @Column(name = "image_3_content_type")
    private String image3ContentType;

    @Lob
    @Column(name = "image_4")
    private byte[] image4;

    @Column(name = "image_4_content_type")
    private String image4ContentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "from", "projetcs" }, allowSetters = true)
    private Patron patron;

    @ManyToMany
    @JoinTable(
        name = "rel_project__matieres",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "matieres_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sellers", "projects" }, allowSetters = true)
    private Set<Tissu> matieres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Project id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Project name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return this.ref;
    }

    public Project ref(String ref) {
        this.setRef(ref);
        return this;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public LocalDate getCreationDate() {
        return this.creationDate;
    }

    public Project creationDate(LocalDate creationDate) {
        this.setCreationDate(creationDate);
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public byte[] getImage1() {
        return this.image1;
    }

    public Project image1(byte[] image1) {
        this.setImage1(image1);
        return this;
    }

    public void setImage1(byte[] image1) {
        this.image1 = image1;
    }

    public String getImage1ContentType() {
        return this.image1ContentType;
    }

    public Project image1ContentType(String image1ContentType) {
        this.image1ContentType = image1ContentType;
        return this;
    }

    public void setImage1ContentType(String image1ContentType) {
        this.image1ContentType = image1ContentType;
    }

    public byte[] getImage2() {
        return this.image2;
    }

    public Project image2(byte[] image2) {
        this.setImage2(image2);
        return this;
    }

    public void setImage2(byte[] image2) {
        this.image2 = image2;
    }

    public String getImage2ContentType() {
        return this.image2ContentType;
    }

    public Project image2ContentType(String image2ContentType) {
        this.image2ContentType = image2ContentType;
        return this;
    }

    public void setImage2ContentType(String image2ContentType) {
        this.image2ContentType = image2ContentType;
    }

    public byte[] getImage3() {
        return this.image3;
    }

    public Project image3(byte[] image3) {
        this.setImage3(image3);
        return this;
    }

    public void setImage3(byte[] image3) {
        this.image3 = image3;
    }

    public String getImage3ContentType() {
        return this.image3ContentType;
    }

    public Project image3ContentType(String image3ContentType) {
        this.image3ContentType = image3ContentType;
        return this;
    }

    public void setImage3ContentType(String image3ContentType) {
        this.image3ContentType = image3ContentType;
    }

    public byte[] getImage4() {
        return this.image4;
    }

    public Project image4(byte[] image4) {
        this.setImage4(image4);
        return this;
    }

    public void setImage4(byte[] image4) {
        this.image4 = image4;
    }

    public String getImage4ContentType() {
        return this.image4ContentType;
    }

    public Project image4ContentType(String image4ContentType) {
        this.image4ContentType = image4ContentType;
        return this;
    }

    public void setImage4ContentType(String image4ContentType) {
        this.image4ContentType = image4ContentType;
    }

    public Patron getPatron() {
        return this.patron;
    }

    public void setPatron(Patron patron) {
        this.patron = patron;
    }

    public Project patron(Patron patron) {
        this.setPatron(patron);
        return this;
    }

    public Set<Tissu> getMatieres() {
        return this.matieres;
    }

    public void setMatieres(Set<Tissu> tissus) {
        this.matieres = tissus;
    }

    public Project matieres(Set<Tissu> tissus) {
        this.setMatieres(tissus);
        return this;
    }

    public Project addMatieres(Tissu tissu) {
        this.matieres.add(tissu);
        tissu.getProjects().add(this);
        return this;
    }

    public Project removeMatieres(Tissu tissu) {
        this.matieres.remove(tissu);
        tissu.getProjects().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Project)) {
            return false;
        }
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Project{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", ref='" + getRef() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", image1='" + getImage1() + "'" +
            ", image1ContentType='" + getImage1ContentType() + "'" +
            ", image2='" + getImage2() + "'" +
            ", image2ContentType='" + getImage2ContentType() + "'" +
            ", image3='" + getImage3() + "'" +
            ", image3ContentType='" + getImage3ContentType() + "'" +
            ", image4='" + getImage4() + "'" +
            ", image4ContentType='" + getImage4ContentType() + "'" +
            "}";
    }
}
