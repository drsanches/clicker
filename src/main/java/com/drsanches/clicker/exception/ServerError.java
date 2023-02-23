package com.drsanches.clicker.exception;

import java.util.UUID;

public class ServerError extends RuntimeException {

    private final String uuid = UUID.randomUUID().toString();

    public ServerError(String message) {
        super(message);
    }

    public ServerError(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return "{\"uuid\": \"" + uuid + "\", \"message\": \"" + super.getMessage() + "\"}";
    }
}
