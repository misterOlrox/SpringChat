package com.olrox.chat.exception;

import com.olrox.chat.entity.User;

public class AlreadySignedInException extends RuntimeException {
    private User user;

    public AlreadySignedInException(User user) {
        super();
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
