package com.example.securityservice.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFound(EntityNotFoundException e){
        ErrorResponse response = new ErrorResponse("User not found", LocalDateTime.now());
        return  new ResponseEntity<>(response, HttpStatusCode.valueOf(404));
    }
}
