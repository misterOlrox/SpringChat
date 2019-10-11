package com.olrox.chat.repository;

import com.olrox.chat.tcp.Connection;
import org.springframework.stereotype.Repository;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ConnectionRepository {
    private final Map<Long, Object> userIdMap = new ConcurrentHashMap<>();
    private final Map<Object, Long> connectionMap = new ConcurrentHashMap<>();

    public synchronized void save(Long userId, Object connection) {
        if(connection instanceof Connection
                || connection instanceof Session) {
            connectionMap.put(connection, userId);
            userIdMap.put(userId, connection);
            return;
        }

        throw new RuntimeException("Object wasn't a socket connection or websocket session");
    }

    public Object getConnectionBy(Long userId) {
        return userIdMap.get(userId);
    }

    public Long getUserIdBy(Object connection) {
        if(connection instanceof Connection
                || connection instanceof Session) {
            return connectionMap.get(connection);
        }

        throw new RuntimeException("Object wasn't a socket connection or websocket session");
    }

    public synchronized void removeBy(Long userId) {
        Object connection = userIdMap.remove(userId);
        connectionMap.remove(connection);
    }

    public synchronized Object replace(Long unauthorizedId, Long loggedId) {
        Object connection = getConnectionBy(unauthorizedId);
        userIdMap.put(loggedId, connection);
        connectionMap.put(connection, loggedId);

        return connection;
    }
}
