package com.olrox.chat.service;

import com.olrox.chat.entity.User;
import com.olrox.chat.repository.SocketConnectionRepository;
import com.olrox.chat.repository.WebSocketSessionRepository;
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

    @Autowired
    private MessageService messageService;

    public void addWebSocketSession(Long userId, Session session) {
        webSocketSessionRepository.put(userId, session);
    }

    public void addSocketConnection(Long userId, Connection connection) {
        socketConnectionRepository.put(userId, connection);
    }
}
