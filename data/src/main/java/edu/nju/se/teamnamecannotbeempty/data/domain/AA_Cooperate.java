package edu.nju.se.teamnamecannotbeempty.data.domain;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name= "aa_cooperate")
public class AA_Cooperate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(foreignKey = @ForeignKey(name = "AA_COOPERATE_AUTHOR1"))
    private Author author1;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(foreignKey = @ForeignKey(name = "AA_COOPERATE_AUTHOR2"))
    private Author author2;
    @Column(name = "year")
    private Integer year;

    public AA_Cooperate(Author author1, Author author2, Integer year) {
        this.author1 = author1;
        this.author2 = author2;
        this.year = year;
    }

    public AA_Cooperate() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Author getAuthor1() {
        return author1;
    }

    public void setAuthor1(Author author1) {
        this.author1 = author1;
    }

    public Author getAuthor2() {
        return author2;
    }

    public void setAuthor2(Author author2) {
        this.author2 = author2;
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
        AA_Cooperate that = (AA_Cooperate) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(author1, that.author1) &&
                Objects.equals(author2, that.author2) &&
                Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author1, author2, year);
    }

    @Override
    public String toString() {
        return "AA_Cooperate{" +
                "id=" + id +
                ", author1=" + author1 +
                ", author2=" + author2 +
                ", year=" + year +
                '}';
    }
}
