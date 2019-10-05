package com.olrox.chat.service;

import com.olrox.chat.entity.User;
import com.olrox.chat.repository.SocketConnectionRepository;
import com.olrox.chat.repository.WebSocketSessionRepository;
import com.olrox.chat.service.sending.GeneralSender;
import com.olrox.chat.tcp.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.websocket.Session;

@Service
public class ConnectionService {

    @Autowired
    private WebSocketSessionRepository webSocketSessionRepository;

    @Autowired
    private SocketConnectionRepository socketConnectionRepository;

    public void addWebSocketSession(User user, Session session) {
        webSocketSessionRepository.put(user.getId(), session);
    }

    public void addSocketConnection(User user, Connection connection) {
        socketConnectionRepository.put(user.getId(), connection);
    }

    public void closeWebSocketSession(User user) {
        webSocketSessionRepository.remove(user.getId());
    }

    public void closeSocketConnection(User user) {
        socketConnectionRepository.remove(user.getId());
    }
}
