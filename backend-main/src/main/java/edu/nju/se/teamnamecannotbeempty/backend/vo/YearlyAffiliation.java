package edu.nju.se.teamnamecannotbeempty.backend.vo;

public class YearlyAffiliation {
    private String affName;
    private int year;
    private long affId;

    public YearlyAffiliation(String affName, int year, long affId) {
        this.affName = affName;
        this.year = year;
        this.affId = affId;
    }
}
