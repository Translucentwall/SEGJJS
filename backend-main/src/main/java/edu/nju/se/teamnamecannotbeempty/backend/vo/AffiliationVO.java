package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.Objects;

public class AffiliationVO {
    // 机构名，包括部门/院系名和组织/单位名（如软件学院of南京大学）
    private String name;
    private long id;

    public AffiliationVO(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AffiliationVO that = (AffiliationVO) o;
        return id == that.id &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, id);
    }
}
