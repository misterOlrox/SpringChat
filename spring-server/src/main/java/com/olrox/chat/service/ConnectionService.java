package com.olrox.chat.service;

import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.User;
import com.olrox.chat.repository.ConnectionRepository;
import com.olrox.chat.repository.UserRepository;
import com.olrox.chat.tcp.Connection;
import org.springframework.stereotype.Service;

import javax.websocket.Session;

@Service
public class ConnectionService {

    private final ConnectionRepository connectionRepository;
    private final UserRepository userRepository;

    public ConnectionService(ConnectionRepository connectionRepository,
                             UserRepository userRepository) {
        this.connectionRepository = connectionRepository;
        this.userRepository = userRepository;
    }

    public void addConnection(User user, Object connection) {
        connectionRepository.save(user.getId(), connection);
    }

    public void closeConnection(User user) {
        connectionRepository.removeBy(user.getId());
    }

    public long getUserIdBy(Object connection) {
        return connectionRepository.getUserIdBy(connection);
    }

    public void replaceConnection(User unauthorized, User logged) {
        Object connection = connectionRepository.replace(unauthorized.getId(), logged.getId());
        if(connection instanceof Session) {
            logged.setConnectionType(ConnectionType.WEBSOCKET);
        } else if(connection instanceof Connection) {
            logged.setConnectionType(ConnectionType.SOCKET);
        }

        userRepository.save(logged);
    }
}
