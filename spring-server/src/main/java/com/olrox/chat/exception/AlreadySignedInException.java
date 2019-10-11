package com.olrox.chat.exception;

public class AlreadySignedInException extends RuntimeException {
    public AlreadySignedInException(String message) {
        super(message);
    }
}
