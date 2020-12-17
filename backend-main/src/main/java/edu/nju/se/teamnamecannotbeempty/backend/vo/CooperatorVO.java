package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.List;
import java.util.Objects;

/**
 * 合作关系预测返回的vo，包含作者id及名称，作者最近所在机构id及名称，
 * 以及与所查询作者相同的所有研究方向id及名称
 * @author laiba
 */
public class CooperatorVO {
    private Long id;
    private String name;
    private Long affiliationId;
    private String affiliation;
    /**
     * 合作的可能性=((1.2/(上一次合作至今的年份差+1))[未合作过设为0] + 0.8) * (相同的研究方向数+1)
     */
    private double possibility;
    /**
     * 研究方向的hot均为0
     */
    private List<TermItem> termList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAffiliationId() {
        return affiliationId;
    }

    public void setAffiliationId(Long affiliationId) {
        this.affiliationId = affiliationId;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public List<TermItem> getTermList() {
        return termList;
    }

    public void setTermList(List<TermItem> termList) {
        this.termList = termList;
    }

    public double getPossibility() {
        return possibility;
    }

    public void setPossibility(double possibility) {
        this.possibility = possibility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CooperatorVO that = (CooperatorVO) o;
        return Double.compare(that.possibility, possibility) == 0 &&
                Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(affiliationId, that.affiliationId) &&
                Objects.equals(affiliation, that.affiliation) &&
                Objects.equals(termList, that.termList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, affiliationId, affiliation, possibility, termList);
    }

    @Override
    public String toString() {
        return "CooperatorVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", affiliationId=" + affiliationId +
                ", affiliation='" + affiliation + '\'' +
                ", possibility=" + possibility +
                ", termList=" + termList +
                '}';
    }
}
