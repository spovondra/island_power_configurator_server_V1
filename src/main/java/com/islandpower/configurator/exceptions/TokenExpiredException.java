package com.islandpower.configurator.exceptions;

/**
 * Exception thrown when a JWT token has expired.
 */
public class TokenExpiredException extends RuntimeException {

    /**
     * Constructs the exception with a specified message.
     *
     * @param message - the detail message
     */
    public TokenExpiredException(String message) {
        super(message);
    }

    /**
     * Constructs the exception with a specified message and cause.
     *
     * @param message - the detail message
     * @param cause   - the cause of the exception
     */
    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
