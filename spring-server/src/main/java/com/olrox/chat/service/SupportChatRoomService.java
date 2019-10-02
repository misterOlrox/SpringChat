package com.olrox.chat.service;

import com.olrox.chat.entity.*;
import com.olrox.chat.repository.MessageRepository;
import com.olrox.chat.repository.RoleRepository;
import com.olrox.chat.repository.SupportChatRoomRepository;
import com.olrox.chat.service.sending.GeneralSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SupportChatRoomService {

    @Autowired
    private SupportChatRoomRepository supportChatRoomRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private GeneralSender generalSender;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageDetailsService messageDetailsService;

    private SupportChatRoom createNewChat(User creator, Role.Type roleType) {
        SupportChatRoom dialogue = new SupportChatRoom();

        List<User> users = new ArrayList<>();
        users.add(creator);
        dialogue.setUserList(users);

        if (roleType.equals(Role.Type.AGENT)) {
            dialogue.setState(SupportChatRoom.State.NEED_CLIENT);
        } else {
            dialogue.setState(SupportChatRoom.State.NEED_AGENT);
        }

        dialogue.setCreationDate(LocalDateTime.now());
        dialogue = supportChatRoomRepository.save(dialogue);

        Role role = new Role();
        role.setChatRoom(dialogue);
        role.setUser(creator);
        role.setType(roleType);
        roleRepository.save(role);

        return dialogue;
    }

    public void directUserToChat(User user) {
        SupportChatRoom chat;

        Role.Type role = user.getCurrentRoleType();

        switch(role) {
            case CLIENT:
                chat = supportChatRoomRepository.findFirstByStateOrderByCreationDateAsc(SupportChatRoom.State.NEED_CLIENT);

                if(chat == null) {
                    chat = createNewChat(user, role);
                    generalSender.send(messageService.createInfoMessage(user,
                            "We haven't free agents. You can write messages and they will be delivered."));
                } else {
                    addToChat(user, chat);
                }
                return;
            case AGENT:
                chat = supportChatRoomRepository.findFirstByStateOrderByCreationDateAsc(SupportChatRoom.State.NEED_AGENT);

                if(chat == null) {
                    chat = createNewChat(user, role);
                    generalSender.send(messageService.createInfoMessage(user,
                            "There aren't free clients. Wait..."));
                } else {
                    addToChat(user, chat);
                }
        }





    }

    public void addToChat(User user, SupportChatRoom chat) {
        user.getChatRooms().add(chat);
        chat.getUserList().add(user);
        chat.setState(SupportChatRoom.State.FULL);

        supportChatRoomRepository.save(chat);
    }

    public void broadcast(Message message) {
        User sender = message.getSender();

        // FIXME искать среди своих комнат
        SupportChatRoom chatRoom = supportChatRoomRepository
                .findFirstByStateOrderByCreationDateDesc(SupportChatRoom.State.FULL);

        message.setChatRoom(chatRoom);
        List<MessageDetail> messageDetails = new ArrayList<>();

        // FIXME получателей может не быть
        List<User> recipients = chatRoom.getUserList();

        for(User recipient : recipients) {
            MessageDetail detail = messageDetailsService.create(message, recipient, MessageDetail.Status.NOT_RECEIVED);
            messageDetails.add(detail);
        }

        message.setMessageDetails(messageDetails);
        messageRepository.save(message);

        addMessageToHistory(chatRoom, message);

        generalSender.send(message);
    }

    private void addMessageToHistory(SupportChatRoom chatRoom, Message message) {
        List<Message> messageHistory = chatRoom.getMessageHistory();

        if(messageHistory == null) {
            messageHistory = new ArrayList<>();
        }

        messageHistory.add(message);
        supportChatRoomRepository.save(chatRoom);
    }
}
