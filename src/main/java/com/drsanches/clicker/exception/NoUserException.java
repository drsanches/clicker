package com.drsanches.clicker.exception;

import java.util.UUID;

public class NoUserException extends RuntimeException {

    private final String uuid = UUID.randomUUID().toString();

    public NoUserException(String message) {
        super(message);
    }

    public NoUserException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return "{\"uuid\": \"" + uuid + "\", \"message\": \"" + super.getMessage() + "\"}";
    }
}
