package com.udacity.maar.popularmovies;

/**
 * Created by maar on 2/22/2016.
 */
public class Review {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    private String id;
    private String content;
    private String author;

}
