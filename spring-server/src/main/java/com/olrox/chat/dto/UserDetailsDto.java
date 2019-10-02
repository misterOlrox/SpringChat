package com.olrox.chat.dto;

import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.Role;

import java.util.Objects;

public class UserDetailsDto {

    private Long id;
    private String name;
    private Role.Type currentRoleType;
    private ConnectionType connectionType;

    public UserDetailsDto() {
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

    public Role.Type getCurrentRoleType() {
        return currentRoleType;
    }

    public void setCurrentRoleType(Role.Type currentRoleType) {
        this.currentRoleType = currentRoleType;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsDto that = (UserDetailsDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                currentRoleType == that.currentRoleType &&
                connectionType == that.connectionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, currentRoleType, connectionType);
    }
}
