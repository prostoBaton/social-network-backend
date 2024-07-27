package com.example.publicationservice.exception;

public class UnableToUpdateException extends RuntimeException{
    public UnableToUpdateException(String message) {
        super(message);
    }
}
