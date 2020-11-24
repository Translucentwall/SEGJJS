package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.visualization;

import java.util.HashMap;
import java.util.List;

public class KeywordsYear {
    private Integer year;
    private List<String> keywords;
    private HashMap<String,Integer> keywords_counts;
    private String keyword;

    KeywordsYear(Integer year,List<String> keywords){
        this.year=year;
        this.keywords=keywords;
        keywords_counts=new HashMap<>();
        for(String keyword:keywords){
            keywords_counts.put(keyword,1);
        }
    }

    public Integer getYear() {
        return year;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void addKeywords(List<String> terms){
        for(int i=0;i<terms.size();i++){
            if(!keywords.contains(terms.get(i))){
                keywords.add(terms.get(i));
                keywords_counts.put(terms.get(i),1);
            }
            else{
                Integer newCount=keywords_counts.get(terms.get(i))+1;
                keywords_counts.put(terms.get(i),newCount);
            }

        }
        getKeyword();
    }

    public String getKeyword(){
        String answer="";
        Integer value=0;
        for(String key:keywords_counts.keySet()){
            if(value<keywords_counts.get(key)){
                value=keywords_counts.get(key);
                answer=key;
            }
        }
        this.keyword=answer;
        return  keyword;
    }

    public Integer getCount(String keyword){
        return keywords_counts.get(keyword);
    }

}