package com.example.publicationservice.exception;

public class PublicationNotFoundException extends RuntimeException{
    public PublicationNotFoundException(String message) {
        super(message);
    }
}
