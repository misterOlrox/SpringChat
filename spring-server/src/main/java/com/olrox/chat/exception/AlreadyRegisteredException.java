package com.olrox.chat.exception;

import com.olrox.chat.entity.User;

public class AlreadyRegisteredException extends RuntimeException {
    private User user;

    public AlreadyRegisteredException(User user) {
        super();
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
