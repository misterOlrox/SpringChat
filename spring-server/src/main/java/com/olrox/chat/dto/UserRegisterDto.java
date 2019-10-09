package com.olrox.chat.dto;

import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.User;

public class UserRegisterDto {
    private String name;
    private Role.Type role;
    private String password;

    public UserRegisterDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role.Type getRole() {
        return role;
    }

    public void setRole(Role.Type role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
