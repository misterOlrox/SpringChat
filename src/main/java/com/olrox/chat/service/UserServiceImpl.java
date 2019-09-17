package com.olrox.chat.service;

import com.olrox.chat.session.ChatSession;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final static UsersData usersData = new UsersData();

    UserServiceImpl() {
    }

    @Override
    public ChatSession pollFreeClient() {
        return usersData.pollFreeClient();
    }

    @Override
    public ChatSession pollFreeAgent() {
        return usersData.pollFreeAgent();
    }

    @Override
    public void addFreeClient(ChatSession client) {
        usersData.addFreeClient(client);
    }

    @Override
    public void addFreeAgent(ChatSession agent) {
        usersData.addFreeAgent(agent);
    }

    @Override
    public boolean addOnlineUser(String username) {
        return usersData.addOnlineUser(username);
    }

    @Override
    public void removeOnlineUser(String username) {
        usersData.removeOnlineUser(username);
    }
}
