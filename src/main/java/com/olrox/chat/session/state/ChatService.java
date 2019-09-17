package com.olrox.chat.session.state;

import com.olrox.chat.entity.Message;

public interface ChatService {

    void register(Message message);

    void sendMessage(Message message);

    void leave();

    void exit();

}
