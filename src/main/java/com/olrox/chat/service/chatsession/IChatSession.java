package com.olrox.chat.service.chatsession;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.User;

public interface IChatSession {
    void send(Message message);

    User getUser();
}
