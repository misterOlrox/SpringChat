package com.olrox.chat.dto;

import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.User;

public class DetailedUserDto {
    private Long id;
    private String name;
    private String currentRoleType;
    private ConnectionType connectionType;

    public DetailedUserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.currentRoleType = user.getCurrentRoleType().toString().toLowerCase();
        this.connectionType = user.getConnectionType();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentRoleType() {
        return currentRoleType;
    }

    public void setCurrentRoleType(String currentRoleType) {
        this.currentRoleType = currentRoleType;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }
}
