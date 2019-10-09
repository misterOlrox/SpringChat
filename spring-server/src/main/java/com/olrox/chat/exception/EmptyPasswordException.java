package com.olrox.chat.exception;

public class EmptyPasswordException extends RuntimeException {
    public EmptyPasswordException(String message) {
        super(message);
    }
}
