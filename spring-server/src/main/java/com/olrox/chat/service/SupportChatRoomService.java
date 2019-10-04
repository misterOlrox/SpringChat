package com.olrox.chat.service;

import com.olrox.chat.entity.*;
import com.olrox.chat.repository.MessageRepository;
import com.olrox.chat.repository.RoleRepository;
import com.olrox.chat.repository.SupportChatRoomRepository;
import com.olrox.chat.repository.UserRepository;
import com.olrox.chat.service.sending.GeneralSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private UserRepository userRepository;

    @Autowired
    private GeneralSender generalSender;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageDetailService messageDetailService;

    private SupportChatRoom createNewChat(User creator) {
        Role.Type roleType = creator.getCurrentRoleType();
        SupportChatRoom dialogue = new SupportChatRoom();

        if (roleType.equals(Role.Type.AGENT)) {
            dialogue.setState(SupportChatRoom.State.NEED_CLIENT);
            dialogue.setAgent(creator);
        } else {
            dialogue.setState(SupportChatRoom.State.NEED_AGENT);
            dialogue.setClient(creator);
        }

        dialogue.setCreationDate(LocalDateTime.now());
        dialogue = supportChatRoomRepository.save(dialogue);

        Role role = new Role();
        role.setChatRoom(dialogue);
        role.setUser(creator);
        role.setType(roleType);
        roleRepository.save(role);

        creator.getChatRooms().add(dialogue);
        userRepository.save(creator);

        return dialogue;
    }

    @Transactional
    public SupportChatRoom directUserToChat(User user) {
        SupportChatRoom chat = null;

        Role.Type role = user.getCurrentRoleType();

        switch(role) {
            case CLIENT:
                chat = supportChatRoomRepository.findFirstByStateOrderByCreationDateAsc(SupportChatRoom.State.NEED_CLIENT);

                if(chat == null) {
                    chat = createNewChat(user);
                    generalSender.send(messageService.createInfoMessage(user,
                            "We haven't free agents. You can write messages and they will be delivered."));
                } else {
                    addToChat(user, chat);
                }
                return chat;
            case AGENT:
                chat = supportChatRoomRepository.findFirstByStateOrderByCreationDateAsc(SupportChatRoom.State.NEED_AGENT);

                if(chat == null) {
                    chat = createNewChat(user);
                    generalSender.send(messageService.createInfoMessage(user,
                            "There aren't free clients. Wait..."));
                } else {
                    addToChat(user, chat);
                }
        }

        return chat;
    }

    @Transactional
    public void addToChat(User user, SupportChatRoom chat) {
        user.getChatRooms().add(chat);

        userRepository.save(user);

        // FIXME to roleService
        Role.Type roleInChat = user.getCurrentRoleType();

        Role role = new Role();
        role.setType(roleInChat);
        role.setChatRoom(chat);
        role.setUser(user);
        roleRepository.save(role);

        if(roleInChat == Role.Type.AGENT) {
            chat.setAgent(user);
            notifyAboutNewCompanion(chat);
            for (Message message : chat.getMessageHistory()) {
                MessageDetail messageDetail = messageDetailService.create(message, user, MessageDetail.Status.NOT_RECEIVED);
                message.getMessageDetails().add(messageDetail);
                messageRepository.save(message);

                generalSender.send(message);
            }
        } else {
            chat.setClient(user);
            notifyAboutNewCompanion(chat);
        }

        chat.setState(SupportChatRoom.State.FULL);
        supportChatRoomRepository.save(chat);
    }

    private void notifyAboutNewCompanion(SupportChatRoom chatRoom) {
        User agent = chatRoom.getAgent();
        User client = chatRoom.getClient();

        Message messageToAgent = messageService.createInfoMessage(agent, "Now you are chatting with client " + client.getName());
        Message messageToClient = messageService.createInfoMessage(client, "Now you are chatting with agent " + agent.getName());

        generalSender.send(messageToAgent);
        generalSender.send(messageToClient);
    }

    @Transactional
    public void broadcast(Message message) {
        User sender = message.getSender();
        Role.Type senderRole = sender.getCurrentRoleType();

        SupportChatRoom currentRoom;

        if(senderRole == Role.Type.CLIENT) {
            currentRoom = supportChatRoomRepository.findFirstByClientEqualsOrderByCreationDateDesc(sender);
        } else {
            currentRoom = supportChatRoomRepository.findFirstByAgentEqualsOrderByCreationDateDesc(sender);
        }

        message.setChatRoom(currentRoom);

        if(currentRoom == null || currentRoom.getState() == SupportChatRoom.State.CLOSED) {
            currentRoom = directUserToChat(sender);
        }

        addMessageToHistory(currentRoom, message);

        if (currentRoom.getState() == SupportChatRoom.State.NEED_AGENT) {
            // nothing to do
        } else if(currentRoom.getState() == SupportChatRoom.State.NEED_CLIENT) {
            Message infoResponse = messageService.createInfoMessage(sender, currentRoom,
                    "There aren't free clients. Wait...");
            addMessageToHistory(currentRoom, infoResponse);
            generalSender.send(infoResponse);
        } else if(currentRoom.getState() == SupportChatRoom.State.FULL) {
            User recipient = currentRoom.getCompanionFor(senderRole);
            MessageDetail detail = messageDetailService.create( message,
                                                                recipient,
                                                                MessageDetail.Status.NOT_RECEIVED);
            message.addMessageDetail(detail);
            generalSender.send(message);
        }
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
