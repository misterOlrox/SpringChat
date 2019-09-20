package com.olrox.chat.service.sending;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import org.json.JSONObject;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Objects;

public class WsChatSessionAdapter implements ChatSession {
    private final Session session;

    public WsChatSessionAdapter(Session session) {
        this.session = session;
    }

    @Override
    public synchronized void send(Message message) {
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

    @Override
    public synchronized boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WsChatSessionAdapter that = (WsChatSessionAdapter) o;
        return session.equals(that.session);
    }

    @Override
    public int hashCode() {
        return Objects.hash(session);
    }
}
