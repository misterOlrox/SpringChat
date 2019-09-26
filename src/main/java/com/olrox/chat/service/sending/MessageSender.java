package com.olrox.chat.service.sending;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.User;

public interface MessageSender {

    void send(Message message, User user);

}
