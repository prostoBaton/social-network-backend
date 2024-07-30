package com.example.publicationservice.dto;

import java.io.Serializable;

public class RabbitDto implements Serializable {
    private String email;
    private String message;

    public RabbitDto(String email, String message) {
        this.email = email;
        this.message = message;
    }

    public RabbitDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
