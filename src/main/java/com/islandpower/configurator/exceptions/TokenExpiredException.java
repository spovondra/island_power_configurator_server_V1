package com.islandpower.configurator.exceptions;

/**
 * Výjimka po vypršení JWT tokenu.
 */
public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
