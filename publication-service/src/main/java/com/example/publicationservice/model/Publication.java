package com.example.publicationservice.model;



import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Document(collection = "publication")
public class Publication implements Serializable {
    private String id;
    private int userId;
    private String text;

    private Set<Integer> likes;
    private List<Comment> comments;
    private LocalDateTime createdAt;

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

    public Optional<Set<Integer>> getLikes() {
        return Optional.ofNullable(likes);
    }

    public void setLikes(Set<Integer> likes) {
        this.likes = likes;
    }

    public Optional<List<Comment>> getComments() {
        return Optional.ofNullable(comments);
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
