package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.enumeration.Editors;
import com.mycompany.myapp.domain.enumeration.Language;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PatronEditor.
 */
@Entity
@Table(name = "patron_editor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PatronEditor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "print_date")
    private LocalDate printDate;

    @Column(name = "number")
    private String number;

    @Enumerated(EnumType.STRING)
    @Column(name = "editor")
    private Editors editor;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PatronEditor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PatronEditor name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getPrintDate() {
        return this.printDate;
    }

    public PatronEditor printDate(LocalDate printDate) {
        this.setPrintDate(printDate);
        return this;
    }

    public void setPrintDate(LocalDate printDate) {
        this.printDate = printDate;
    }

    public String getNumber() {
        return this.number;
    }

    public PatronEditor number(String number) {
        this.setNumber(number);
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Editors getEditor() {
        return this.editor;
    }

    public PatronEditor editor(Editors editor) {
        this.setEditor(editor);
        return this;
    }

    public void setEditor(Editors editor) {
        this.editor = editor;
    }

    public Language getLanguage() {
        return this.language;
    }

    public PatronEditor language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public PatronEditor price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public byte[] getImage() {
        return this.image;
    }

    public PatronEditor image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public PatronEditor imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatronEditor)) {
            return false;
        }
        return id != null && id.equals(((PatronEditor) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatronEditor{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", printDate='" + getPrintDate() + "'" +
            ", number='" + getNumber() + "'" +
            ", editor='" + getEditor() + "'" +
            ", language='" + getLanguage() + "'" +
            ", price=" + getPrice() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
