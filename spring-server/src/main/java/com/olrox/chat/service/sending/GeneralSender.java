package com.olrox.chat.service.sending;

import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.Message;
import com.olrox.chat.repository.MessageRepository;
import com.olrox.chat.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeneralSender {

    @Autowired
    private SocketSender socketSender;

    @Autowired
    private WebSocketSender webSocketSender;

    @Autowired
    private MessageService messageService;

    public void send(Message message) {
        ConnectionType connectionType = message.getRecipient().getConnectionType();

        switch (connectionType) {
            case SOCKET:
                socketSender.send(message);
                messageService.markMessageAsDelivered(message);
                return;
            case WEBSOCKET:
                webSocketSender.send(message);
                messageService.markMessageAsDelivered(message);
        }
    }

}
