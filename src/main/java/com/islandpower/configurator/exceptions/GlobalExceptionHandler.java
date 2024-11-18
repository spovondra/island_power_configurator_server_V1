package com.islandpower.configurator.exceptions;

import com.mongodb.MongoSocketOpenException;
import com.mongodb.MongoSocketReadTimeoutException;
import com.mongodb.MongoTimeoutException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MongoSocketOpenException.class)
    public ResponseEntity<String> handleMongoSocketOpenException(MongoSocketOpenException e) {
        System.err.println("MongoDB connection error: " + e.getMessage()); // log
        return new ResponseEntity<>("Unable to connect to MongoDB. Please try again later.", HttpStatus.SERVICE_UNAVAILABLE); // error response
    }

    @ExceptionHandler(MongoSocketReadTimeoutException.class)
    public ResponseEntity<String> handleMongoSocketReadTimeoutException(MongoSocketReadTimeoutException e) {
        System.err.println("MongoDB timeout error: " + e.getMessage());
        return new ResponseEntity<>("The connection to MongoDB timed out. Please try again later.", HttpStatus.REQUEST_TIMEOUT);
    }

    @ExceptionHandler(MongoTimeoutException.class)
    public ResponseEntity<String> handleMongoTimeoutException(MongoTimeoutException ex) {
        System.err.println("MongoDB timeout exception: " + ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.REQUEST_TIMEOUT)
                .body("MongoDB connection timed out. Please check your configuration or try again later.");
    }

    @ExceptionHandler(DataAccessResourceFailureException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessResourceFailureException ex) {
        System.err.println("MongoDB data access error: " + ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("MongoDB is unavailable. Please check the database status or try again later.");
    }
}
