package com.olrox.chat.dto;

import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.User;

public class DetailedUserDto extends UserDto {
    private ConnectionType connectionType;

    public DetailedUserDto(User user) {
        super(user);
        this.connectionType = user.getConnectionType();
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }
}
