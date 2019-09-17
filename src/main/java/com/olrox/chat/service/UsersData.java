package com.olrox.chat.service;

import com.olrox.chat.exception.InvalidUserStateException;
import com.olrox.chat.message.author.ServerAsAuthor;
import com.olrox.chat.session.ChatSession;
import com.olrox.chat.session.state.FreeAgentService;
import com.olrox.chat.session.state.FreeClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

class UsersData {

    private final static Logger LOGGER = LogManager.getLogger(UsersData.class);

    private final Queue<ChatSession> freeClients = new ArrayDeque<>();
    private final Queue<ChatSession> freeAgents = new ArrayDeque<>();
    private final Set<String> onlineUsers = new HashSet<>();

    UsersData(){
        addOnlineUser(ServerAsAuthor.getInstance().getName());
    }

    private synchronized void removeOfflineClients(){
        ChatSession freeClient = freeClients.peek();
        while(freeClient != null && !onlineUsers.contains(freeClient.getUserName())){
            freeClients.poll();
            freeClient = freeClients.peek();
        }
    }

    private synchronized void removeOfflineAgents(){
        ChatSession freeAgent = freeAgents.peek();
        while(freeAgent != null && !onlineUsers.contains(freeAgent.getUserName())){
            freeAgents.poll();
            freeAgent = freeAgents.peek();
        }
    }

    public synchronized ChatSession pollFreeClient(){
        removeOfflineClients();
        LOGGER.debug("Free clients: " + freeClients);

        ChatSession client = freeClients.poll();

        if(client != null) {
            LOGGER.debug("Free client was removed.");
        } else {
            LOGGER.debug("No free client in queue.");
        }

        return client;
    }

    public synchronized ChatSession pollFreeAgent(){
        removeOfflineAgents();
        LOGGER.debug("Free agents: " + freeAgents);

        ChatSession agent = freeAgents.poll();

        if(agent != null) {
            LOGGER.debug("Free agent was removed.");
        } else {
            LOGGER.debug("No free agent in queue.");
        }

        return agent;
    }

    public synchronized void addFreeClient(ChatSession client){
        if(!(client.getState() instanceof FreeClientService)) {
            throw new InvalidUserStateException("User isn't in FreeClientState.");
        }
        freeClients.add(client);

        LOGGER.debug("Free client " + client.getUserName() + " was added.");
        LOGGER.debug("Free clients: " + freeClients);
    }

    public synchronized void addFreeAgent(ChatSession agent) {
        if(!(agent.getState() instanceof FreeAgentService)) {
            throw new InvalidUserStateException("User isn't in FreeAgentState.");
        }
        freeAgents.add(agent);

        LOGGER.debug("Free agent " + agent.getUserName() + " was added.");
        LOGGER.debug("Free agents: " + freeAgents);
    }

    public synchronized boolean addOnlineUser(String username) {
        boolean isNewUser = onlineUsers.add(username);

        if(isNewUser) {
            LOGGER.debug("Online user " + username + " was added: " + onlineUsers);
        } else {
            LOGGER.debug("Online user " + username + " already registered: " + onlineUsers);
        }

        return isNewUser;
    }

    public synchronized void removeOnlineUser(String username) {
        onlineUsers.remove(username);
        LOGGER.debug("Online user " + username + " was removed: " + onlineUsers);
    }
}
