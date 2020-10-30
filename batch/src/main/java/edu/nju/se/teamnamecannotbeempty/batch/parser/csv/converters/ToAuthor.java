package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import com.opencsv.bean.AbstractCsvConverter;
import edu.nju.se.teamnamecannotbeempty.data.domain.Affiliation;
import edu.nju.se.teamnamecannotbeempty.data.domain.Author;

import java.util.Collection;
import java.util.HashMap;

public class ToAuthor{
    private static final HashMap<String, Author> saveMap = new HashMap<>();

    public static Author getAuthor(String name){
        name=name.trim();
        if(name.isEmpty()) {
            return null;
        }
        String lowercase = name.toLowerCase();
        Author result = saveMap.get(lowercase);
        if (result == null) {
            synchronized (saveMap) {
                if ((result = saveMap.get(lowercase)) == null) {
                    result = new Author();
                    result.setName(name);
                    result.setLowerCaseName(lowercase);
                    saveMap.put(lowercase, result);
                }
            }
        }
        return result;
    }

    public static Collection<Author> getSaveCollection() {
        return saveMap.values();
    }

}
