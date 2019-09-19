package com.olrox.chat.entity;

import com.olrox.chat.service.chatsession.IChatSession;

import java.util.UUID;

public class User {
    private UUID id;
    private String name;
    private IChatSession chatSession;

    public User(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public IChatSession getChatSession() {
        return chatSession;
    }

    public void setChatSession(IChatSession chatSession) {
        this.chatSession = chatSession;
    }
}
