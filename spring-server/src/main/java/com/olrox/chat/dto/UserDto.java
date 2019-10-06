package com.olrox.chat.dto;

import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.User;

public class UserDto {
    private Long id;
    private String name;
    private Role.Type role;

    public UserDto() {
    }

    public UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.role = user.getCurrentRoleType();
    }

    public UserDto(Long id, String name, Role.Type role) {
        this.id = id;
        this.name = name;
        this.role = role;
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

    public Role.Type getRole() {
        return role;
    }

    public void setRole(Role.Type role) {
        this.role = role;
    }
}
