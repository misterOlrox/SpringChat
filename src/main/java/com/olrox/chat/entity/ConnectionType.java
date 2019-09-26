package com.olrox.chat.entity;

import static com.olrox.chat.entity.ConnectionType.TypeConstants.*;

public enum ConnectionType {
    OFFLINE(OFFLINE_SENDER),
    SOCKET(SOCKET_SENDER),
    WEBSOCKET(WEBSOCKET_SENDER);

    private final String senderName;

    ConnectionType(String senderName) {
        this.senderName = senderName;
    }

    @Override
    public String toString() {
        return senderName;
    }

    public String getSenderName() {
        return senderName;
    }

    public interface TypeConstants {

        String OFFLINE_SENDER = "offlineSender";
        String SOCKET_SENDER = "socketSender";
        String WEBSOCKET_SENDER = "websocketSender";
    }
}