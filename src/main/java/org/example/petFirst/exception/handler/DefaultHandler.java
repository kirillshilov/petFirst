package org.example.petFirst.exception.handler;

import org.example.petFirst.exception.UserNotFound;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultHandler {
    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFound userNotFound) {
        return ResponseEntity.status(404).body(userNotFound.getMessage());
    }
}
