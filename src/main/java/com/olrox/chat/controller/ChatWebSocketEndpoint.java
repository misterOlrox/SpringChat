package com.olrox.chat.controller;

import com.olrox.chat.config.CustomSpringConfigurator;
import com.olrox.chat.controller.util.ChatCommand;
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
    private ConnectionService connectionService;

    @OnOpen
    public void onOpen(Session session) {

        ChatSession chatSession = new WsChatSessionAdapter(session);
        connectionService.addNewChatSession(chatSession);
    }

    @OnClose
    public void onClose(Session session) {

    }

    @OnMessage
    public void onMessage(Session session, String text) {
        for (ChatCommand chatCommand : chatCommands) {
            if (chatCommand.checkMatch(text)) {
                chatCommand.execute(new WsChatSessionAdapter(session), text);
                return;
            }
        }
    }

    private final ChatCommand[] chatCommands = new ChatCommand[]{
            // /register
            new ChatCommand("\\/register .+") {
                @Override
                public void execute(ChatSession chatSession, String text) {

                }
            },

            // /leave
            new ChatCommand("\\/leave") {
                @Override
                public void execute(ChatSession chatSession, String text) {

                }
            },

            // /exit
            new ChatCommand("\\/exit") {
                @Override
                public void execute(ChatSession chatSession, String text) {

                }
            },

            // message
            new ChatCommand(".*") {
                @Override
                public void execute(ChatSession chatSession, String text) {
                }
            }
    };
}
