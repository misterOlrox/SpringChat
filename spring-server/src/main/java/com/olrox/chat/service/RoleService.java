package com.olrox.chat.service;

import com.olrox.chat.entity.ChatRoom;
import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.User;
import com.olrox.chat.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role create(ChatRoom chatRoom, User user, Role.Type roleType) {
        Role role = new Role();
        role.setChatRoom(chatRoom);
        role.setUser(user);
        role.setType(roleType);

        return roleRepository.save(role);
    }
}
