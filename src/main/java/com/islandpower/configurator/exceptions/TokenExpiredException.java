package com.islandpower.configurator.exceptions;

/**
 * Exception thrown when a JWT token has expired.
 */
public class TokenExpiredException extends RuntimeException {

    /**
     * Constructs a new TokenExpiredException with the specified message.
     *
     * @param message The message explaining the reason for the exception
     */
    public TokenExpiredException(String message) {
        super(message);
    }

    /**
     * Constructs a new TokenExpiredException
     *
     * @param message The message explaining the reason for the exception
     * @param cause The cause of the exception
     */
    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
