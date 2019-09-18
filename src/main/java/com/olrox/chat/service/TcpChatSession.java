package com.olrox.chat.service;

import com.olrox.chat.custom.controller.tcp.Connection;
import com.olrox.chat.entity.message.Message;

public class TcpChatSession implements IChatSession{
    private Connection connection;

    public TcpChatSession(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void send(Message message) {
        String response = "[" + message.getSender().getName() + "] : " + message.getText() + '\n';
        connection.send(response.getBytes());
    }
}
