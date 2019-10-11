package com.olrox.chat.service.sending;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.User;
import com.olrox.chat.repository.ConnectionRepository;
import com.olrox.chat.tcp.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SocketSender {

    private final ConnectionRepository connectionRepository;

    public SocketSender(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    public void send(Message message, User sender, User recipient) {
        String senderName = null;

        if(sender == null) {
            senderName = "Server";
        } else if(sender.getId() == recipient.getId()) {
            senderName = "You";
        } else {
            senderName = sender.getName();
        }

        String response = "[" + senderName + "] : " + message.getText() + '\n';

        long recipientId = recipient.getId();
        Connection connection = (Connection) connectionRepository.getConnectionBy(recipientId);

        connection.send(response.getBytes());
    }
}
