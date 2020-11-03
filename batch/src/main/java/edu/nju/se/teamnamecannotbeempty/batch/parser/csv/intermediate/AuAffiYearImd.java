package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.intermediate;

import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;
import edu.nju.se.teamnamecannotbeempty.data.domain.AuthorAffiliationYear;

import java.util.Objects;

public class AuAffiYearImd {
    private Author author;
    private Affiliation affiliation;
    private Integer year;

    public AuAffiYearImd(Author author, Affiliation affiliation, Integer year) {
        this.author = author;
        this.affiliation = affiliation;
        this.year = year;
    }

    public AuthorAffiliationYear toAuthorAffiliationYear(){
        AuthorAffiliationYear authorAffiliationYear=new AuthorAffiliationYear();
        authorAffiliationYear.setAuthor(author);
        authorAffiliationYear.setAffiliation(affiliation);
        authorAffiliationYear.setYear(year);
        return authorAffiliationYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuAffiYearImd that = (AuAffiYearImd) o;
        return Objects.equals(author.getLowerCaseName(), that.author.getLowerCaseName()) &&
                Objects.equals(affiliation.getFormattedName(), that.affiliation.getFormattedName()) &&
                Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, affiliation, year);
    }
}
