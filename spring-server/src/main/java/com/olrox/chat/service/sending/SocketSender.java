package com.olrox.chat.service.sending;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.User;
import com.olrox.chat.repository.SocketConnectionRepository;
import com.olrox.chat.tcp.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketSender {

    @Autowired
    private SocketConnectionRepository socketConnectionRepository;

    public void send(Message message) {
        User recipient = message.getRecipient();
        long recipientId = recipient.getId();

        String senderName = message.getType() == MessageType.SERVER_INFO ? "Server" : recipient.getName();

        String response = "[" + senderName + "] : " + message.getText() + '\n';

        Connection connection = socketConnectionRepository.get(recipientId);

        connection.send(response.getBytes());
    }
}
