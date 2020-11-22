package edu.nju.se.teamnamecannotbeempty.backend.vo;

import java.util.List;
import java.util.Objects;

public class PaperVO {
    private Long id;

    private String title;

    private List<Author_AffiliationVO> authorAffiliationVOS;

    private String conferenceTitle;

    private Long conferenceId;

    private String year;
    /**
     * 摘要，abstract重名了
     */
    private String summary;

    private String doi;

    private List<String> authorKeywords;

    private Integer citationCount;

    private Integer referenceCount;

    public PaperVO(long id, String title, List<Author_AffiliationVO> authorAffiliationVOS,
                   String year, String summary, String doi, List<String> authorKeywords,
                   Integer citationCount, Integer referenceCount) {
        this.id = id;
        this.title = title;
        this.authorAffiliationVOS = authorAffiliationVOS;
        this.year=year;
        this.summary = summary;
        this.doi = doi;
        this.authorKeywords = authorKeywords;
        this.citationCount = citationCount;
        this.referenceCount = referenceCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author_AffiliationVO> getAuthorAffiliationVOS() {
        return authorAffiliationVOS;
    }

    public void setAuthorAffiliationVOS(List<Author_AffiliationVO> authorAffiliationVOS) {
        this.authorAffiliationVOS = authorAffiliationVOS;
    }

    public Long getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(Long conferenceId) {
        this.conferenceId = conferenceId;
    }

     public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getAuthorKeywords() {
        return authorKeywords;
    }

    public void setAuthorKeywords(List<String> authorKeywords) {
        this.authorKeywords = authorKeywords;
    }

     public Integer getCitationCount() {
        return citationCount;
    }

    public void setCitationCount(Integer citationCount) {
        this.citationCount = citationCount;
    }

    public Integer getReferenceCount() {
        return referenceCount;
    }

    public void setReferenceCount(Integer referenceCount) {
        this.referenceCount = referenceCount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConferenceTitle() {
        return conferenceTitle;
    }

    public void setConferenceTitle(String conferenceTitle) {
        this.conferenceTitle = conferenceTitle;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDoi() {
        return doi;
    }

    public void setDoi(String doi) {
        this.doi = doi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PaperVO paperVO = (PaperVO) o;
        return Objects.equals(id, paperVO.id) &&
                Objects.equals(title, paperVO.title) &&
                Objects.equals(authorAffiliationVOS, paperVO.authorAffiliationVOS) &&
                Objects.equals(conferenceTitle, paperVO.conferenceTitle) &&
                Objects.equals(conferenceId, paperVO.conferenceId) &&
                Objects.equals(year, paperVO.year) &&
                Objects.equals(summary, paperVO.summary) &&
                Objects.equals(doi, paperVO.doi) &&
                Objects.equals(authorKeywords, paperVO.authorKeywords) &&
                Objects.equals(citationCount, paperVO.citationCount) &&
                Objects.equals(referenceCount, paperVO.referenceCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, authorAffiliationVOS,
                conferenceTitle, conferenceId, year, summary, doi,
                authorKeywords, citationCount, referenceCount);
    }
}
