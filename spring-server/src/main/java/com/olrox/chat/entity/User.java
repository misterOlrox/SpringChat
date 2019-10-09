package com.olrox.chat.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void addChatRoom(ChatRoom chatRoom) {
        if(getChatRooms() == null) {
            setChatRooms(new ArrayList<>());
        }
        chatRooms.add(chatRoom);
    }
}
