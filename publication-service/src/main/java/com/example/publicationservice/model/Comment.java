package com.example.publicationservice.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private String username;
    private String text;
    //TODO image(blob) storage


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
