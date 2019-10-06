package com.olrox.chat.chatcontroller;

import com.olrox.chat.chatcontroller.handler.CommandHandler;
import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.ConnectionService;
import com.olrox.chat.service.UserService;
import com.olrox.chat.tcp.Connection;
import com.olrox.chat.tcp.annotation.OnTcpConnect;
import com.olrox.chat.tcp.annotation.OnTcpDisconnect;
import com.olrox.chat.tcp.annotation.OnTcpMessage;
import com.olrox.chat.tcp.annotation.TcpController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@TcpController
public class SocketController {

    private final Map<Connection, Long> connections = new ConcurrentHashMap<>();

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

    @OnTcpConnect
    public void connect(Connection connection) {
        User user = userService.addUnauthorizedUser(ConnectionType.SOCKET);
        long userId = user.getId();
        connections.put(connection, userId);
        connectionService.addSocketConnection(user, connection);

        userService.sendFirstMessages(user);
    }

    @OnTcpDisconnect
    public void disconnect(Connection connection) {
        Long userId = connections.get(connection);
        User user = userService.getUserById(userId);
        userService.handleExit(user);
        connections.remove(connection);
        connectionService.closeSocketConnection(user);
    }

    @OnTcpMessage
    public void receiveMessage(Connection connection, String data) {
        Long userId = connections.get(connection);
        User user = userService.getUserById(userId);

        for (CommandHandler commandHandler : commandHandlers) {
            if (commandHandler.checkMatch(data)) {
                commandHandler.handleCommand(user, data);
                return;
            }
        }
    }
}
