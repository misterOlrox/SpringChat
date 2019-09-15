package com.olrox.chat.manager;

import com.olrox.chat.user.User;

public class UsersManagerImpl implements UsersManager{
    private final static UsersData usersData = new UsersData();

    UsersManagerImpl() {
    }

    @Override
    public User pollFreeClient() {
        return usersData.pollFreeClient();
    }

    @Override
    public User pollFreeAgent() {
        return usersData.pollFreeAgent();
    }

    @Override
    public void addFreeClient(User client) {
        usersData.addFreeClient(client);
    }

    @Override
    public void addFreeAgent(User agent) {
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
