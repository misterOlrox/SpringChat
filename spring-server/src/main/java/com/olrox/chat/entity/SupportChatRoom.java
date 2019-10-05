package com.olrox.chat.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class SupportChatRoom extends ChatRoom {

    public enum State {
        CLOSED,
        NEED_AGENT,
        NEED_CLIENT,
        FULL
    }

    @OneToOne
    private User client;

    @OneToOne
    private User agent;

    @Enumerated(EnumType.STRING)
    private State state;

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public User getCompanionFor(User user) {
        if (user == null || agent == null || client == null) {
            return null;
        } else if (user.getId() == client.getId()) {
            return agent;
        } else if (user.getId() == agent.getId()) {
            return client;
        } else {
            return null;
        }
    }
}
