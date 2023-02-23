package com.drsanches.clicker.exception;

import java.util.UUID;

public class AuthException extends RuntimeException {

    private final String uuid = UUID.randomUUID().toString();

    public AuthException(String message) {
        super(message);
    }

    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return "{\"uuid\": \"" + uuid + "\", \"message\": \"" + super.getMessage() + "\"}";
    }
}
