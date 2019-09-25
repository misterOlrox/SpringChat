package com.olrox.chat.service.sending;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.User;

public interface ChatSession {
    void send(Message message);

    long getUserId();

    void setUserId(long userId);
}
