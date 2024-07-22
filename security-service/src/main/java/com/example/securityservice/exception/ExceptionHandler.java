package com.example.securityservice.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionHandler {

    public ResponseEntity<ErrorResponce> userNotFound(EntityNotFoundException e){
        ErrorResponce responce = new ErrorResponce("User not found", LocalDateTime.now());
        return  new ResponseEntity<>(responce, HttpStatusCode.valueOf(404));
    }
}
