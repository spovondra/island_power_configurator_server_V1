package com.islandpower.configurator.exceptions;

import com.mongodb.MongoSocketOpenException;
import com.mongodb.MongoSocketReadTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MongoSocketOpenException.class)
    public ResponseEntity<String> handleMongoSocketOpenException(MongoSocketOpenException e) {
        System.err.println("MongoDB connection error: " + e.getMessage()); //log

        return new ResponseEntity<>("Could not connect to MongoDB. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE); // error return
    }

    @ExceptionHandler(MongoSocketReadTimeoutException.class)
    public ResponseEntity<String> handleMongoSocketReadTimeoutException(MongoSocketReadTimeoutException e) {
        System.err.println("MongoDB timeout error: " + e.getMessage());
        return new ResponseEntity<>("Connection to MongoDB timed out. Please try again later.", HttpStatus.REQUEST_TIMEOUT);
    }
}
