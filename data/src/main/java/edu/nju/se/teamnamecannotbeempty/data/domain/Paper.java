package edu.nju.se.teamnamecannotbeempty.data.domain;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "papers", indexes = @Index(name = "YEAR_DESC", columnList = "year DESC"))
@SuppressWarnings("unused")
public class Paper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 论文的代理主键
    private Long id;
    @Column(nullable = false, length = 1000)
    // 论文的标题
    private String title;
    @ElementCollection(fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.DETACH)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_AA_PAPER"))
    // 发表论文的每个作者-机构构成的对象的列表
    private List<Author_Affiliation> aa = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_PAPER_CONFERENCE"))
    // 会议对象。对应数据中的出版物
    private Conference conference;

    @Column(name = "abstract", columnDefinition = "TEXT")
    // 摘要。对应数据中的abstract
    private String summary;

    private String doi;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(foreignKey = @ForeignKey(name = "FK_AUTHOR_KEYWORDS_PAPER"), inverseForeignKey = @ForeignKey(name = "FK_AUTHOR_KEYWORDS_TERM"))
    @Fetch(FetchMode.SUBSELECT)
    // 作者给出的关键字
    private List<Term> author_keywords = new ArrayList<>();
    // 被引数
    private Integer citation;
    // 引文数
    private Integer reference;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "referer", orphanRemoval = true, fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<Ref> refs = new ArrayList<>();
    //出版年份
    private Integer year;
    @Transient
    //用于hibernate search高亮年份
    private String year_highlight;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "paper")
    @Fetch(FetchMode.SUBSELECT)
    private List<Popularity> pops = new ArrayList<>();

    public Paper() {
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Paper.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("title='" + title + "'")
                .add("aa=" + aa)
                .add("conference=" + conference)
                .add("year=" + year)
                .toString();
    }

    @Entity(name = "paper_popularity")
    @Table(indexes = {
            @Index(name = "POPULARITY_DESC", columnList = "popularity DESC"),
            @Index(name = "YEAR", columnList = "year")
    })
    public static class Popularity implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @ManyToOne(optional = false)
        @JoinColumn(foreignKey = @ForeignKey(name = "FK_POP_PAPER"))
        private Paper paper;
        @ColumnDefault("0.0")
        private Double popularity;
        private Integer year;

        public Popularity(Paper paper, Double popularity) {
            this(paper, popularity, paper.year);
        }

        public Popularity(Paper paper, Double popularity, Integer year) {
            this.paper = paper;
            this.popularity = popularity;
            this.year = year;
        }

        public Popularity() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Popularity that = (Popularity) o;
            return Objects.equals(paper, that.paper) &&
                    Objects.equals(year, that.year);
        }

        @Override
        public int hashCode() {
            return Objects.hash(paper, year);
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Paper getPaper() {
            return paper;
        }

        public void setPaper(Paper paper) {
            this.paper = paper;
        }

        public Double getPopularity() {
            return popularity;
        }

        public void setPopularity(Double popularity) {
            this.popularity = popularity;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Paper paper = (Paper) o;
        if(doi!=null&&paper.doi!=null){
            return Objects.equals(doi, paper.doi);
        }else if(id!=null&&paper.id!=null){
            return Objects.equals(id,paper.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(doi == null ? id==null? UUID.randomUUID() :id : doi);
    }

    /**
     * 向引用列表中添加引用对象，务必使用这个方法
     *
     * @param ref 引用对象
     */
    public void addRef(Ref ref) {
        refs.add(ref);
        ref.setReferer(this);
    }

    /**
     * 从引用列表中删除引用，务必使用这个方法
     *
     * @param ref 引用对象
     */
    public void removeRef(Ref ref) {
        refs.remove(ref);
        ref.setReferer(null);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author_Affiliation> getAa() {
        return aa;
    }

    public void setAa(List<Author_Affiliation> aa) {
        this.aa = aa;
    }

    public Conference getConference() {
        return conference;
    }

    public void setConference(Conference conference) {
        this.conference = conference;
    }


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }


    public List<Term> getAuthor_keywords() {
        return author_keywords;
    }

    public void setAuthor_keywords(List<Term> author_keywords) {
        this.author_keywords = author_keywords;
    }

    public Integer getCitation() {
        return citation;
    }

    public void setCitation(Integer citation) {
        this.citation = citation;
    }

    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
    }

    public List<Ref> getRefs() {
        return refs;
    }

    public void setRefs(List<Ref> refs) {
        this.refs = refs;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getYear_highlight() {
        return year_highlight;
    }

    public void setYear_highlight(String year_highlight) {
        this.year_highlight = year_highlight;
    }

    public List<Popularity> getPops() {
        return pops;
    }

    public void setPops(List<Popularity> pops) {
        this.pops = pops;
    }
}
