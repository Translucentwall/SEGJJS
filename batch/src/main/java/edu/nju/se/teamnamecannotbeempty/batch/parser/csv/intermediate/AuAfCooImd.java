package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.intermediate;

import edu.nju.se.teamnamecannotbeempty.data.domain.AA_Cooperate;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;

import java.util.Objects;

public class AuAfCooImd {
    private Author author1;
    private Author author2;
    private Integer year;

    public AuAfCooImd(Author author1, Author author2, Integer year) {
        this.author1 = author1;
        this.author2 = author2;
        this.year = year;
    }

    public AA_Cooperate toAA_Cooperate(){
        AA_Cooperate aaCooperate=new AA_Cooperate();
        aaCooperate.setAuthor1(author1);
        aaCooperate.setAuthor2(author2);
        aaCooperate.setYear(year);
        return aaCooperate ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuAfCooImd that = (AuAfCooImd) o;

        return Objects.equals(author1.getLowerCaseName(), that.author1.getLowerCaseName()) &&
                Objects.equals(author2.getLowerCaseName(), that.author2.getLowerCaseName()) &&
                Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author1, author2, year);
    }
}
