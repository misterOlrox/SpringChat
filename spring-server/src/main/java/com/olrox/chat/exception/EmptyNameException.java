package com.olrox.chat.exception;

import com.olrox.chat.entity.User;

public class EmptyNameException extends RuntimeException {
    private User user;

    public EmptyNameException(User user) {
        super();
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
