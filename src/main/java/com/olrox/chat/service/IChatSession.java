package com.olrox.chat.service;

import com.olrox.chat.entity.message.Message;

public interface IChatSession {
    void send(Message message);
}
