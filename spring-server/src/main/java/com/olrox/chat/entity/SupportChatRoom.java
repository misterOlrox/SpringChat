package com.olrox.chat.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
public class SupportChatRoom extends ChatRoom {

    public enum State {
        CLOSED,
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
