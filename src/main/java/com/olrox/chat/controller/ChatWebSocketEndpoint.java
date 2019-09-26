package com.olrox.chat.controller;

import com.olrox.chat.config.CustomSpringConfigurator;
import com.olrox.chat.controller.util.ChatCommand;
import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.ConnectionService;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.UserService;
import com.olrox.chat.service.sending.MessageSender;
import com.olrox.chat.service.sending.MessageSenderFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat", configurator = CustomSpringConfigurator.class)
public class ChatWebSocketEndpoint {

    private final Map<Session, Long> sessions = new ConcurrentHashMap<>();

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageSenderFactory messageSenderFactory;

    @OnOpen
    public void onOpen(Session session) {
        User user = userService.addUnauthorizedUser(ConnectionType.WEBSOCKET);
        long userId = user.getId();
        sessions.put(session, userId);
        connectionService.addWebSocketSession(userId, session);

        MessageSender messageSender = messageSenderFactory.getMessageSender(ConnectionType.WEBSOCKET);
        messageSender.send(messageService.createGreetingMessage(user), user);
        messageSender.send(messageService.createRegisterInfoMessage(user), user);
    }

    @OnClose
    public void onClose(Session session) {

    }

    @OnMessage
    public void onMessage(Session session, String text) {
        Long userId = sessions.get(session);
        User user = userService.getUserById(userId);
        Message message = messageService.createUserMessage(user, text);

        for (ChatCommand chatCommand : chatCommands) {
            if (chatCommand.checkMatch(text)) {
                chatCommand.execute(user, message);
                return;
            }
        }
    }

    private final ChatCommand REGISTER = new ChatCommand("\\/register .+") {
        @Override
        public void execute(User user, Message message) {
        }
    };

    private final ChatCommand LEAVE = new ChatCommand("\\/leave") {
        @Override
        public void execute(User user, Message message) {

        }
    };

    private final ChatCommand EXIT = new ChatCommand("\\/exit") {
        @Override
        public void execute(User user, Message message) {

        }
    };

    private final ChatCommand SEND_MESSAGE = new ChatCommand(".*") {
        @Override
        public void execute(User user, Message message) {
        }
    };

    private final ChatCommand[] chatCommands = new ChatCommand[]{
            REGISTER,
            LEAVE,
            EXIT,
            SEND_MESSAGE
    };
}
