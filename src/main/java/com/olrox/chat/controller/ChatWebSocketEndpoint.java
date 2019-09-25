package com.olrox.chat.controller;

import com.olrox.chat.config.CustomSpringConfigurator;
import com.olrox.chat.controller.util.ChatCommand;
import com.olrox.chat.service.ChatSessionService;
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
    private ChatSessionService chatSessionService;

    @OnOpen
    public void onOpen(Session session) {
        session.addMessageHandler();

        ChatSession chatSession = new WsChatSessionAdapter(session);
        chatSessionService.addNewChatSession(chatSession);
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

    private final ChatCommand REGISTER = new ChatCommand("\\/register .+") {
        @Override
        public void execute(ChatSession chatSession, String text) {
        }
    };

    private final ChatCommand LEAVE = new ChatCommand("\\/leave") {
        @Override
        public void execute(ChatSession chatSession, String text) {

        }
    };

    private final ChatCommand EXIT = new ChatCommand("\\/exit") {
        @Override
        public void execute(ChatSession chatSession, String text) {

        }
    };

    private final ChatCommand SEND_MESSAGE = new ChatCommand(".*") {
        @Override
        public void execute(ChatSession chatSession, String text) {
        }
    };

    private final ChatCommand[] chatCommands = new ChatCommand[]{
            REGISTER,
            LEAVE,
            EXIT,
            SEND_MESSAGE
    };
}
