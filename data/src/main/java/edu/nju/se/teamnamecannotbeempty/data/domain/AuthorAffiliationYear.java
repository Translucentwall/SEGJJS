package edu.nju.se.teamnamecannotbeempty.data.domain;

import javax.persistence.*;

@Entity
@Table(name= "author_affiliation_year")
public class AuthorAffiliationYear {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(foreignKey = @ForeignKey(name = "AUTHOR_AFFILIATION_AUTHOR"))
    private Author author;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(foreignKey = @ForeignKey(name = "AUTHOR_AFFILIATION_AFFILIATION"))
    private Affiliation affiliation;
    private Integer year;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Affiliation getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(Affiliation affiliation) {
        this.affiliation = affiliation;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AuthorAffiliationYear that = (AuthorAffiliationYear) o;

        if (id != null ? !id.equals(that.id) : that.id != null) {
            return false;
        }
        if (year != null ? !year.equals(that.year) : that.year != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (year != null ? year.hashCode() : 0);
        return result;
    }
}
