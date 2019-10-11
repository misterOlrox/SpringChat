package com.olrox.chat.service.sending;

import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.User;
import com.olrox.chat.entity.MessageDetail;
import com.olrox.chat.service.MessageDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GeneralSender {

    private final SocketSender socketSender;

    private final WebSocketSender webSocketSender;

    private final MessageDetailService messageDetailService;

    public GeneralSender(SocketSender socketSender, WebSocketSender webSocketSender, MessageDetailService messageDetailService) {
        this.socketSender = socketSender;
        this.webSocketSender = webSocketSender;
        this.messageDetailService = messageDetailService;
    }

    public void send(Message message) {
        List<MessageDetail> details = message.getMessageDetails();
        User sender = message.getSender();

        for(MessageDetail detail : details) {
            if(detail.getStatus().equals(MessageDetail.Status.NOT_RECEIVED)) {
                User recipient = detail.getUser();
                ConnectionType connectionType = recipient.getConnectionType();

                switch (connectionType) {
                    case SOCKET:
                        socketSender.send(message, sender, recipient);
                        messageDetailService.markAsReceived(detail);
                        return;
                    case WEBSOCKET:
                        webSocketSender.send(message, sender, recipient);
                        messageDetailService.markAsReceived(detail);
                }
            }
        }
    }

    public void resendAllFor(User recipient) {
        List<MessageDetail> details = messageDetailService.findAllFor(recipient);
        ConnectionType connectionType = recipient.getConnectionType();

        switch (connectionType) {
            case SOCKET:
                for(MessageDetail detail : details) {
                    Message message = detail.getMessage();
                    User sender = message.getSender();
                    socketSender.send(message, sender, recipient);
                    if(detail.getStatus() == MessageDetail.Status.NOT_RECEIVED) {
                        messageDetailService.markAsReceived(detail);
                    }
                }
                return;
            case WEBSOCKET:
                for(MessageDetail detail : details) {
                    Message message = detail.getMessage();
                    User sender = message.getSender();
                    webSocketSender.send(message, sender, recipient);
                    if(detail.getStatus() == MessageDetail.Status.NOT_RECEIVED) {
                        messageDetailService.markAsReceived(detail);
                    }
                }
        }
    }

}
