package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Seller.
 */
@Entity
@Table(name = "seller")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Seller implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "web_site")
    private String webSite;

    @ManyToMany
    @JoinTable(
        name = "rel_seller__tissu",
        joinColumns = @JoinColumn(name = "seller_id"),
        inverseJoinColumns = @JoinColumn(name = "tissu_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sellers", "projects" }, allowSetters = true)
    private Set<Tissu> tissus = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Seller id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Seller name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebSite() {
        return this.webSite;
    }

    public Seller webSite(String webSite) {
        this.setWebSite(webSite);
        return this;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public Set<Tissu> getTissus() {
        return this.tissus;
    }

    public void setTissus(Set<Tissu> tissus) {
        this.tissus = tissus;
    }

    public Seller tissus(Set<Tissu> tissus) {
        this.setTissus(tissus);
        return this;
    }

    public Seller addTissu(Tissu tissu) {
        this.tissus.add(tissu);
        tissu.getSellers().add(this);
        return this;
    }

    public Seller removeTissu(Tissu tissu) {
        this.tissus.remove(tissu);
        tissu.getSellers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seller)) {
            return false;
        }
        return id != null && id.equals(((Seller) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Seller{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", webSite='" + getWebSite() + "'" +
            "}";
    }
}
