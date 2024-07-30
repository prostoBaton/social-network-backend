package com.example.notificationservice.dto;

import java.io.Serializable;

public class RabbitDto implements Serializable {
    private String email;
    private String message;

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
