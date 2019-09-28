package com.olrox.chat.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
public class ClientAgentDialogue extends ChatRoom {

    public enum State {
        INACTIVE,
        NEED_AGENT,
        NEED_CLIENT,
        FULL
    }

    @Enumerated(EnumType.STRING)
    private State state;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
