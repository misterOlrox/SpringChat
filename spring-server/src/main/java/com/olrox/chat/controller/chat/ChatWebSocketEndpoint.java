package com.olrox.chat.controller.chat;

import com.olrox.chat.config.CustomSpringConfigurator;
import com.olrox.chat.controller.chat.handler.CommandHandler;
import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.ConnectionService;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.UserService;
import com.olrox.chat.service.sending.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import javax.annotation.PostConstruct;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
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
    @Qualifier(ConnectionType.TypeConstants.WEBSOCKET_SENDER)
    private MessageSender messageSender;

    @Autowired
    private List<CommandHandler> commandHandlers;

    @PostConstruct
    public void init() {
        commandHandlers.sort(AnnotationAwareOrderComparator.INSTANCE);
    }

    @OnOpen
    public void onOpen(Session session) {
        User user = userService.addUnauthorizedUser(ConnectionType.WEBSOCKET);
        long userId = user.getId();
        sessions.put(session, userId);
        connectionService.addWebSocketSession(userId, session);

        messageSender.send(messageService.createGreetingMessage(user));
        messageSender.send(messageService.createRegisterInfoMessage(user));
    }

    @OnClose
    public void onClose(Session session) {

    }

    @OnMessage
    public void onMessage(Session session, String data) {
        Long userId = sessions.get(session);
        User user = userService.getUserById(userId);

        for (CommandHandler commandHandler : commandHandlers) {
            if (commandHandler.checkMatch(data)) {
                commandHandler.handleCommand(user, data);
                return;
            }
        }

        // TODO exception?
    }
}
