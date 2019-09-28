package com.olrox.chat.service;

import com.olrox.chat.entity.ChatRoom;
import com.olrox.chat.entity.ClientAgentDialogue;
import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.User;
import com.olrox.chat.repository.ChatRoomRepository;
import com.olrox.chat.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatRoomService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private RoleRepository roleRepository;

    public ChatRoom createNewClientAgentDialogue(User creator, Role.Type roleType) {
        ClientAgentDialogue dialogue = new ClientAgentDialogue();

        List<User> users = new ArrayList<>();
        users.add(creator);
        dialogue.setUserList(users);

        if (roleType.equals(Role.Type.AGENT)) {
            dialogue.setState(ClientAgentDialogue.State.NEED_CLIENT);
        } else {
            dialogue.setState(ClientAgentDialogue.State.NEED_AGENT);
        }

        dialogue.setCreationDate(LocalDateTime.now());
        dialogue = chatRoomRepository.save(dialogue);

        Role role = new Role();
        role.setChatRoom(dialogue);
        role.setUser(creator);
        role.setType(roleType);
        roleRepository.save(role);

        return dialogue;
    }
}
