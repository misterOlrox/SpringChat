package com.olrox.chat.controller.util;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.User;

public abstract class ChatCommand {
    private final String regex;

    public ChatCommand(String regex) {
        this.regex = regex;
    }

    public abstract void execute(User user, Message message);

    public boolean checkMatch(String text){
        return text.matches(regex);
    }
}
