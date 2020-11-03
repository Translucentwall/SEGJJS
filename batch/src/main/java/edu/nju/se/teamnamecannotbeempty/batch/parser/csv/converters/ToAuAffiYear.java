package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import edu.nju.se.teamnamecannotbeempty.batch.parser.csv.intermediate.AuAffiYearImd;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;
import edu.nju.se.teamnamecannotbeempty.data.domain.AuthorAffiliationYear;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author laiba
 */
public class ToAuAffiYear {
    private static final HashMap<AuAffiYearImd, AuthorAffiliationYear> saveMap=new HashMap<>();

    public static void saveAuthorAffiliationYear(AuAffiYearImd auAffiYearImd){
        if(saveMap.get(auAffiYearImd)==null){
            saveMap.put(auAffiYearImd,auAffiYearImd.toAuthorAffiliationYear());
        }
    }

    public static Collection<AuthorAffiliationYear> getSaveCollection() {
        return saveMap.values();
    }
}
