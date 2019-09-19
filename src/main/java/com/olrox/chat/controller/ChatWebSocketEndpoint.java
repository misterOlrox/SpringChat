package com.olrox.chat.controller;

import com.olrox.chat.config.CustomSpringConfigurator;
import com.olrox.chat.tcp.TcpServer;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.ConnectionService;
import com.olrox.chat.service.MessageHandlingService;
import com.olrox.chat.service.chatsession.IChatSession;
import com.olrox.chat.service.chatsession.WsChatSession;
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

    @Autowired
    private MessageHandlingService messageHandlingService;

    @OnOpen
    public void onOpen(Session session) {

        IChatSession chatSession = new WsChatSession(session, new User());
        connectionService.addNewChatSession(chatSession);
    }

    @OnClose
    public void onClose() {

    }

    @OnMessage
    public void onMessage(String text) {

    }
}
