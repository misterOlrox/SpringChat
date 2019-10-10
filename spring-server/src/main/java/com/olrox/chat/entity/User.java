package com.olrox.chat.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user_table")
public class User implements UserDetails {
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

    public User() {
    }

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

    @Override
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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(password, user.password) &&
                currentRoleType == user.currentRoleType &&
                connectionType == user.connectionType &&
                Objects.equals(chatRooms, user.chatRooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, currentRoleType, connectionType, chatRooms);
    }
}
