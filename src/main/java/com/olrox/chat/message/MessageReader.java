package com.olrox.chat.message;

import com.olrox.chat.entity.Message;

import java.io.IOException;

public interface MessageReader {
    Message readMessage() throws IOException;

    void close() throws IOException;
}
