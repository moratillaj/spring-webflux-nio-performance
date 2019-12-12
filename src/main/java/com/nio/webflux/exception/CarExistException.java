package com.nio.webflux.exception;

public class CarExistException extends RuntimeException {
    public CarExistException(final String message) {
        super(message);
    }
}
