package com.epam.tishkin.server.advices;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

@ControllerAdvice
public class CustomExceptionsHandler {

    @ExceptionHandler({EntityExistsException.class, EntityNotFoundException.class})
    public ResponseEntity<String> handleEntityExistsAndEntityNotFoundExceptions(PersistenceException e) {
        return ResponseEntity.ok(e.getMessage());
    }
}
