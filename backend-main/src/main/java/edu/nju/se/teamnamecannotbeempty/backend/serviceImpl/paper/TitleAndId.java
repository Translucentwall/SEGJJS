package edu.nju.se.teamnamecannotbeempty.backend.serviceImpl.paper;

public class TitleAndId {
    private String title;
    private Long id;
    public TitleAndId(String title, Long id){
        this.title=title;
        this.id=id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public Long getId() {
        return id;
    }
}
