package com.olrox.chat.controller.handler;

import com.olrox.chat.entity.User;

public interface CommandHandler {
    void handleCommand(User user, String data);

    boolean checkMatch(String data);
}
