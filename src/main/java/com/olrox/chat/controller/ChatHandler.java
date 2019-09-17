package com.olrox.chat.controller;

import com.olrox.chat.message.MessageWriter;

public interface ChatHandler {
    void disconnect();

    MessageWriter getMessageWriter();
}
