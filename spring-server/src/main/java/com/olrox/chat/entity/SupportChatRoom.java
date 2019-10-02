package com.olrox.chat.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SupportChatRoom that = (SupportChatRoom) o;
        return state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), state);
    }
}
