package com.olrox.chat.service.sending;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.User;
import com.olrox.chat.repository.ConnectionRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;

@Component
public class WebSocketSender {

    private final ConnectionRepository connectionRepository;

    public WebSocketSender(ConnectionRepository connectionRepository) {
        this.connectionRepository = connectionRepository;
    }

    public void send(Message message, User sender, User recipient) {

        String senderName;
        if(sender == null) {
            senderName = "Server";
        } else if(sender.getId() == recipient.getId()) {
            senderName = "You";
        } else {
            senderName = sender.getName();
        }

        String data = messageToJson(message, senderName);

        long recipientId = recipient.getId();
        Session session = (Session) connectionRepository.getConnectionBy(recipientId);

        try {
            session.getBasicRemote().sendText(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String messageToJson(Message message, String senderName) {
        String data = String.valueOf(new JSONObject()
                .put("author", senderName)
                .put("text", message.getText())
                .put("time", message.getSendTime()));

        return data;
    }

}
