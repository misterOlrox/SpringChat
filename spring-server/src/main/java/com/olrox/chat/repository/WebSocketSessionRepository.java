package com.olrox.chat.repository;

import org.springframework.stereotype.Repository;

import javax.websocket.Session;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class WebSocketSessionRepository {
    private final Map<Long, Session> map = new ConcurrentHashMap<>();

    public Session put(Long userId, Session session) {
        return map.put(userId, session);
    }

    public Session get(Long userId) {
        return map.get(userId);
    }

    public Session remove(Long userId) {
        return map.remove(userId);
    }
}
