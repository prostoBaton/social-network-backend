package com.example.publicationservice.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(PublicationNotFoundException.class)
    public ResponseEntity<ErrorResponse> publicationNotFound(PublicationNotFoundException e){
        ErrorResponse response = new ErrorResponse(e.getMessage(), LocalDateTime.now());
        return  new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
    }
}
