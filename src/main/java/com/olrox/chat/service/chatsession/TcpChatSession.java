package com.olrox.chat.service.chatsession;

import com.olrox.chat.tcp.Connection;
import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.User;

public class TcpChatSession implements IChatSession{
    private Connection connection;
    private final User user;

    public TcpChatSession(Connection connection, User user) {
        this.connection = connection;
        this.user = user;
    }

    @Override
    public void send(Message message) {
        String sender = message.getType() == MessageType.SERVER_INFO ? "Server" : message.getSender().getName();
        String response = "[" + sender + "] : " + message.getText() + '\n';
        connection.send(response.getBytes());
    }

    @Override
    public User getUser() {
        return user;
    }
}
