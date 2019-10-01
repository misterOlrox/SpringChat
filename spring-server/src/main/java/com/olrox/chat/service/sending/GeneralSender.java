package com.olrox.chat.service.sending;

import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeneralSender {

    @Autowired
    private SocketSender socketSender;

    @Autowired
    private WebSocketSender webSocketSender;

    public void send(Message message) {
        ConnectionType connectionType = message.getRecipient().getConnectionType();

        switch (connectionType) {
            case SOCKET:
                socketSender.send(message);
                return;
            case WEBSOCKET:
                webSocketSender.send(message);
        }
    }

}
