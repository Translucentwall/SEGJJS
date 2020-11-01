package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.intermediate;

import com.alibaba.fastjson.annotation.JSONField;

public class AuthorAffiImd {
    @JSONField(name = "affiliation")
    private String affiliation;
    @JSONField(name="name")
    private String author;

    public AuthorAffiImd() {
    }

    public AuthorAffiImd(String affiliation, String author) {
        this.affiliation = affiliation;
        this.author = author;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
