package com.olrox.chat.message;

import com.olrox.chat.entity.Message;
import com.olrox.chat.message.author.Author;

public interface MessageWriter {
    void write(String string, Author author);

    void write(Message message);

    void close();
}
