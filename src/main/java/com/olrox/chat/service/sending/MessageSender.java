package com.olrox.chat.service.sending;

import com.olrox.chat.entity.Message;

public interface MessageSender {

    void send(Message message);

}
