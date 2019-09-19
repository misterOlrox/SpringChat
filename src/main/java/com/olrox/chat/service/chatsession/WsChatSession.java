package com.olrox.chat.service.chatsession;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.User;
import org.json.JSONObject;

import javax.websocket.Session;
import java.io.IOException;

public class WsChatSession implements IChatSession{
    private User user;
    private Session session;

    public WsChatSession(Session session, User user) {
        this.user = user;
        this.session = session;
    }

    @Override
    public void send(Message message) {
        String sender = message.getType() == MessageType.SERVER_INFO ? "Server" : message.getSender().getName();
        String data = String.valueOf(new JSONObject()
                .put("author", sender)
                .put("text", message.getText())
                .put("time", message.getSendTime()));

        try {
            session.getBasicRemote().sendText(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User getUser() {
        return user;
    }
}
