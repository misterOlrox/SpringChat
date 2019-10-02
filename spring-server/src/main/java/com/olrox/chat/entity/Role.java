package com.olrox.chat.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "role_table")
public class Role {
    public enum Type {
        AGENT,
        CLIENT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToOne
    private User user;

    @ManyToOne
    private ChatRoom chatRoom;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(id, role.id) &&
                type == role.type &&
                Objects.equals(user, role.user) &&
                Objects.equals(chatRoom, role.chatRoom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, user, chatRoom);
    }
}
