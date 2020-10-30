package edu.nju.se.teamnamecannotbeempty.batch.parser.csv.converters;

import com.opencsv.bean.AbstractBeanField;
import edu.nju.se.teamnamecannotbeempty.data.domain.Conference;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToConference{
    private static final HashMap<String, Conference> saveMap = new HashMap<>();
    private static final Conference na;
    private static final Pattern ordnoPat = Pattern.compile("(\\d*(?:1st|2nd|3rd|[0-9]th)) ");

    static {
        na = new Conference();
        na.setName("NA");
        saveMap.put("NA", na);
    }

    public static Conference getConference(String name){
        name=name.trim();
        Conference result = saveMap.get(name);
        if (result == null) {
            synchronized (saveMap) {
                if ((result = saveMap.get(name)) == null) {
                    result = new Conference();
                    result.setName(name);
                    saveMap.put(name, result);
                }
            }
        }
        return result;
    }

    private static Integer findYear(String value) {
        String[] split = value.split(" ", 0);
        Integer year = null;
        for (String s : split) {
            if (s.length() == 4 && s.charAt(0) != '0' && StringUtils.isNumeric(s)) {
                year = Integer.parseInt(s);
                break;
            }
        }
        return year;
    }

    public static Collection<Conference> getSaveCollection() {
        return saveMap.values();
    }
}
