package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.intermediate;

import com.alibaba.fastjson.annotation.JSONField;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToAffiliation;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToAuthor;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToConference;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToTerm;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author_Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.Conference;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;
import edu.nju.se.teamnamecannotbeempty.data.domain.Term;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laiba
 */
public class PaperImd {
    @JSONField(name = "references")
    private List<RefImd> references;
    @JSONField(name = "abstract")
    private String summary;
    @JSONField(name = "doi")
    private String doi;
    @JSONField(name = "title")
    private String title;
    @JSONField(name = "publisher")
    private String publisher;
    @JSONField(name = "author")
    private List<AuthorAffiImd> authorAffiImds;
    @JSONField(name = "keyword")
    private List<String> keywords;
    @JSONField(name = "time")
    private String time;

    public PaperImd() {
    }

    public PaperImd(List<RefImd> references, String summary, String doi,
                    String title, String publisher, List<AuthorAffiImd> authorAffiImds,
                    List<String> keywords, String time) {
        this.references = references;
        this.summary = summary;
        this.doi = doi;
        this.title = title;
        this.publisher = publisher;
        this.authorAffiImds = authorAffiImds;
        this.keywords = keywords;
        this.time = time;
    }

    public List<RefImd> getReferences() {
        return references;
    }

    public void setReferences(List<RefImd> references) {
        this.references = references;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public List<AuthorAffiImd> getAuthorAffiImds() {
        return authorAffiImds;
    }

    public void setAuthorAffiImds(List<AuthorAffiImd> authorAffiImds) {
        this.authorAffiImds = authorAffiImds;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 将Paper中介对象转化为Paper，并产生相应的作者，机构，会议，研究方向对象
     * @return Paper
     */
    public Paper toPaper(){
        Paper paper=new Paper();
        paper.setTitle(title);
        paper.setDoi(doi);
        paper.setSummary(summary);
        if(time!=null) {
            paper.setYear(Integer.parseInt(time.split(" ")[1]));
        }
        List<Author_Affiliation> authorAffiliationList =new ArrayList<>();
        for(AuthorAffiImd authorAffiImd:authorAffiImds){
            authorAffiliationList.add(new Author_Affiliation(ToAuthor.getAuthor(authorAffiImd.getAuthor()),
                    ToAffiliation.getAffiliation(authorAffiImd.getAffiliation())));
        }
        paper.setAa(authorAffiliationList);
        Conference conference= ToConference.getConference(publisher);
        List<Term> termList=new ArrayList<>();
        for(String keyword:keywords){
            termList.add(ToTerm.getTerm(keyword));
        }
        paper.setAuthor_keywords(termList);
        return paper;
    }
}
