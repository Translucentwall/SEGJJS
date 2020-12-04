package edu.nju.se.teamnamecannotbeempty.backend.vo;

/**
 * 同行评审推荐时前端传入的表单信息
 * @author laiba
 */
public class InformationReviewed {
    private String author;
    private String affiliation;
    private String term;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
