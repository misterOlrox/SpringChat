package com.olrox.chat.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role.Type currentRoleType;

    @Enumerated(EnumType.STRING)
    private ConnectionType connectionType;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<ChatRoom> chatRooms;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public List<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public Role.Type getCurrentRoleType() {
        return currentRoleType;
    }

    public void setCurrentRoleType(Role.Type currentRoleType) {
        this.currentRoleType = currentRoleType;
    }

    public boolean isRegistered() {
        return name != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                currentRoleType == user.currentRoleType &&
                connectionType == user.connectionType &&
                Objects.equals(chatRooms, user.chatRooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, currentRoleType, connectionType, chatRooms);
    }
}
