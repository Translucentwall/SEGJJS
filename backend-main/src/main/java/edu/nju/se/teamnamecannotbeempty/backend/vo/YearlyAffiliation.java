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

    public String getAffName() {
        return affName;
    }

    public void setAffName(String affName) {
        this.affName = affName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getAffId() {
        return affId;
    }

    public void setAffId(long affId) {
        this.affId = affId;
    }
}
