package com.olrox.chat.exception;

public class NameIsBusyException extends RuntimeException {
    public NameIsBusyException(String message) {
        super(message);
    }
}
