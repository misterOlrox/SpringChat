package com.olrox.chat.user.state;

import com.olrox.chat.entity.Message;

public interface UserState {
    void register(Message message);

    void sendMessage(Message message);

    void leave();

    void exit();

}
