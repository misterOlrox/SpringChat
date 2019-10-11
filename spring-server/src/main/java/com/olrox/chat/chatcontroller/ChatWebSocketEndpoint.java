package com.olrox.chat.chatcontroller;

import com.olrox.chat.chatcontroller.handler.CommandHandler;
import com.olrox.chat.config.CustomSpringConfigurator;
import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.ConnectionService;
import com.olrox.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import javax.annotation.PostConstruct;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.List;

@ServerEndpoint(value = "/chat", configurator = CustomSpringConfigurator.class)
public class ChatWebSocketEndpoint {
    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserService userService;

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
        connectionService.addConnection(user, session);

        userService.sendFirstMessages(user);
    }

    @OnClose
    public void onClose(Session session) {
        Long userId = connectionService.getUserIdBy(session);
        User user = userService.getUserById(userId);
        userService.handleExit(user);
        connectionService.closeConnection(user);
    }

    @OnMessage
    public void onMessage(Session session, String data) {
        Long userId = connectionService.getUserIdBy(session);
        User user = userService.getUserById(userId);

        for (CommandHandler commandHandler : commandHandlers) {
            if (commandHandler.checkMatch(data)) {
                commandHandler.handleCommand(user, data);
                return;
            }
        }
    }
}
