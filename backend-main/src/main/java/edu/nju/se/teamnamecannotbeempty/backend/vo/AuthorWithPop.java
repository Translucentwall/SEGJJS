package edu.nju.se.teamnamecannotbeempty.backend.vo;

import edu.nju.se.teamnamecannotbeempty.data.domain.Author;

/**
 * 带有热度值的作者VO
 */
public class AuthorWithPop {
    private Author author;
    private double pop;
    public AuthorWithPop(Author author, double pop){
        this.author=author;
        this.pop=pop;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public double getPop() {
        return pop;
    }

    public void setPop(double pop) {
        this.pop = pop;
    }
}
