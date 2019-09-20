package com.olrox.chat.controller;

import com.olrox.chat.config.CustomSpringConfigurator;
import com.olrox.chat.tcp.TcpServer;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.ConnectionService;
import com.olrox.chat.service.sending.ChatSession;
import com.olrox.chat.service.sending.WsChatSessionAdapter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value = "/chat", configurator = CustomSpringConfigurator.class)
public class ChatWebSocketEndpoint {
    @Autowired
    private TcpServer tcpServer;

    @Autowired
    private ConnectionService connectionService;


    @OnOpen
    public void onOpen(Session session) {

        ChatSession chatSession = new WsChatSessionAdapter(session);
        connectionService.addNewChatSession(chatSession);
    }

    @OnClose
    public void onClose() {

    }

    @OnMessage
    public void onMessage(String text) {

    }
}
