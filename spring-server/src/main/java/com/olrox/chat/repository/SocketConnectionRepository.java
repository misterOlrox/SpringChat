package com.olrox.chat.repository;

import com.olrox.chat.tcp.Connection;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SocketConnectionRepository {
    private final Map<Long, Connection> map = new ConcurrentHashMap<>();

    public Connection put(Long userId, Connection connection) {
        return map.put(userId, connection);
    }

    public Connection get(Long userId) {
        return map.get(userId);
    }
}
