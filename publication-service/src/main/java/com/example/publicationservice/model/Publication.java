package com.example.publicationservice.model;


import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Set;


@Document(collection = "publication")
public class Publication {
    private String id;
    private int userId;
    private String text;

    private Set<Integer> likes;
    private List<Comment> comments; //or Map<Integer(userId),String(text)>
    //TODO createdAt;

    public Publication() {
    }

    //TODO image(blob) storage


    public String getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<Integer> getLikes() {
        return likes;
    }

    public void setLikes(Set<Integer> likes) {
        this.likes = likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
