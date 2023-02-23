package com.drsanches.clicker.exception;

import java.util.UUID;

public class UserExistsException extends RuntimeException {

    private final String uuid = UUID.randomUUID().toString();

    public UserExistsException(String message) {
        super(message);
    }

    public UserExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return "{\"uuid\": \"" + uuid + "\", \"message\": \"" + super.getMessage() + "\"}";
    }
}
