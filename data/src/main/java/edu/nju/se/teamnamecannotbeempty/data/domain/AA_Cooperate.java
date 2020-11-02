package edu.nju.se.teamnamecannotbeempty.data.domain;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name= "aa_cooperate")
public class AA_Cooperate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(foreignKey = @ForeignKey(name = "AA_COOPERATE_AUTHOR"))
    private Author author1;
    @Column(name = "author2_id")
    private Long author2Id;

    public AA_Cooperate(Author author1, Long author2Id) {
        this.author1 = author1;
        this.author2Id = author2Id;
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

    public Long getAuthor2Id() {
        return author2Id;
    }

    public void setAuthor2Id(Long author2Id) {
        this.author2Id = author2Id;
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
                Objects.equals(author2Id, that.author2Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author1, author2Id);
    }

    @Override
    public String toString() {
        return "AA_Cooperate{" +
                "id=" + id +
                ", author1=" + author1 +
                ", author2Id=" + author2Id +
                '}';
    }
}
