package com.olrox.chat.service.sending;

import com.olrox.chat.tcp.Connection;
import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.User;

import java.util.Objects;

public class TcpChatSessionAdapter implements ChatSession {
    private final Connection connection;
    private long userId;

    public TcpChatSessionAdapter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public synchronized void send(Message message) {
        String sender = message.getType() == MessageType.SERVER_INFO ? "Server" : message.getSender().getName();
        String response = "[" + sender + "] : " + message.getText() + '\n';
        connection.send(response.getBytes());
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TcpChatSessionAdapter that = (TcpChatSessionAdapter) o;
        return connection.equals(that.connection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(connection);
    }
}
