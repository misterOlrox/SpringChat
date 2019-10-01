package com.olrox.chat.service.sending;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.User;
import com.olrox.chat.repository.WebSocketSessionRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;

@Component
public class WebSocketSender {

    @Autowired
    private WebSocketSessionRepository webSocketConnectionRepository;

    public void send(Message message) {
        String data = messageToJson(message);

        User recipient = message.getRecipient();
        long recipientId = recipient.getId();

        Session session = webSocketConnectionRepository.get(recipientId);

        try {
            session.getBasicRemote().sendText(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String messageToJson(Message message) {
        String sender = message.getType() != MessageType.USER_TO_CHAT ? "Server" : message.getSender().getName();

        String data = String.valueOf(new JSONObject()
                .put("author", sender)
                .put("text", message.getText())
                .put("time", message.getSendTime()));

        return data;
    }

}
