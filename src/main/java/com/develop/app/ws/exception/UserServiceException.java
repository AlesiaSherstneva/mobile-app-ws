package com.develop.app.ws.exception;

import java.io.Serial;

public class UserServiceException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 8384765934493576122L;

    public UserServiceException(String message) {
        super(message);
    }
}