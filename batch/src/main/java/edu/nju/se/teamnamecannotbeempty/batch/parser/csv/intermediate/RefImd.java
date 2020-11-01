package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.intermediate;

import com.alibaba.fastjson.annotation.JSONField;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToAffiliation;
import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters.ToAuthor;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author_Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.Paper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author laiba
 */
public class RefImd {
    @JSONField(name = "authors")
    private List<String> authors;
    @JSONField(name = "title")
    private String title;

    public RefImd(List<String> authors, String title) {
        this.authors = authors;
        this.title = title;
    }

    public RefImd() {
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Paper toPaper(){
        Paper paper=new Paper();
        paper.setTitle(title);
        List<Author_Affiliation> authorAffiliationList =new ArrayList<>();
        for(String author:authors){
            authorAffiliationList.add(new Author_Affiliation(
                    ToAuthor.getAuthor(author), ToAffiliation.getAffiliation("NA")));
        }
        paper.setAa(authorAffiliationList);
        return paper;
    }
}
