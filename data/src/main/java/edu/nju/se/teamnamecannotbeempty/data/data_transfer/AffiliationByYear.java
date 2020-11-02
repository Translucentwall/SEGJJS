package edu.nju.se.teamnamecannotbeempty.data.data_transfer;

/**
 * @author laiba
 */
public class AffiliationByYear {
    private String name;
    private Long Id;
    private Integer year;

    public AffiliationByYear(String name, Long id, Integer year) {
        this.name = name;
        Id = id;
        this.year = year;
    }

    public AffiliationByYear() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
