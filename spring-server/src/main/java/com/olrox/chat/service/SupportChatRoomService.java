package com.olrox.chat.service;

import com.olrox.chat.entity.*;
import com.olrox.chat.repository.SupportChatRoomRepository;
import com.olrox.chat.repository.UserRepository;
import com.olrox.chat.service.sending.GeneralSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SupportChatRoomService {

    @Autowired
    private SupportChatRoomRepository supportChatRoomRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GeneralSender generalSender;

    @Autowired
    private MessageService messageService;

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

        roleService.create(dialogue, creator, roleType);

        creator.addChatRoom(dialogue);
        userRepository.save(creator);

        return dialogue;
    }

    @Transactional
    public SupportChatRoom directUserToChat(User user) {
        SupportChatRoom chat = null;

        Role.Type role = user.getCurrentRoleType();

        switch (role) {
            case CLIENT:
                chat = supportChatRoomRepository
                        .findFirstByStateOrderByCreationDateAsc(SupportChatRoom.State.NEED_CLIENT);

                if (chat == null) {
                    chat = createNewChat(user);
                    generalSender.send(messageService.createInfoMessage(user,
                            "We haven't free agents. You can write messages and they will be delivered."));
                } else {
                    addToChat(user, chat);
                }
                break;
            case AGENT:
                chat = supportChatRoomRepository
                        .findFirstByStateOrderByCreationDateAsc(SupportChatRoom.State.NEED_AGENT);

                if (chat == null) {
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
        user.addChatRoom(chat);
        userRepository.save(user);

        Role.Type roleInChat = user.getCurrentRoleType();

        roleService.create(chat, user, roleInChat);

        if (roleInChat == Role.Type.AGENT) {
            chat.setAgent(user);
            notifyAboutNewCompanion(chat);
            sendMessagesToNewAgent(user, chat.getMessageHistory());
        } else {
            chat.setClient(user);
            notifyAboutNewCompanion(chat);
        }

        chat.setState(SupportChatRoom.State.FULL);
        supportChatRoomRepository.save(chat);
    }

    private void sendMessagesToNewAgent(User newAgent, List<Message> messageHistory) {
        for (Message message : messageHistory) {
            if(message.getType() == MessageType.USER_TO_CHAT) {
                messageDetailService.create(message, newAgent, MessageDetail.Status.NOT_RECEIVED);

                generalSender.send(message);
            }
        }
    }

    private void notifyAboutNewCompanion(SupportChatRoom chatRoom) {
        User agent = chatRoom.getAgent();
        User client = chatRoom.getClient();

        Message messageToAgent = messageService.createInfoMessage(agent,
                "Now you are chatting with client " + client.getName());
        Message messageToClient = messageService.createInfoMessage(client,
                "Now you are chatting with agent " + agent.getName());

        generalSender.send(messageToAgent);
        generalSender.send(messageToClient);
    }

    @Transactional
    public void broadcast(Message message) {
        User sender = message.getSender();
        Role.Type senderRole = sender.getCurrentRoleType();

        SupportChatRoom currentRoom  = getLastChatRoom(sender, senderRole);

        message.setChatRoom(currentRoom);

        if (currentRoom == null || currentRoom.getState() == SupportChatRoom.State.CLOSED) {
            currentRoom = directUserToChat(sender);
        }

        addMessageToHistory(currentRoom, message);

        SupportChatRoom.State currentState = currentRoom.getState();

        if (currentState == SupportChatRoom.State.NEED_AGENT) {
            // nothing to do
        } else if (currentState == SupportChatRoom.State.NEED_CLIENT) {
            Message infoResponse = messageService.createInfoMessage(sender, currentRoom,
                    "There aren't free clients. Wait...");
            addMessageToHistory(currentRoom, infoResponse);
            generalSender.send(infoResponse);
        } else if (currentState == SupportChatRoom.State.FULL) {
            User recipient = currentRoom.getCompanionFor(senderRole);
            messageDetailService.create(message,
                    recipient,
                    MessageDetail.Status.NOT_RECEIVED);
            generalSender.send(message);
        }
    }

    private SupportChatRoom getLastChatRoom(User sender, Role.Type senderRole) {
        if (senderRole == Role.Type.CLIENT) {
            return supportChatRoomRepository.findFirstByClientEqualsOrderByCreationDateDesc(sender);
        } else if(senderRole == Role.Type.AGENT) {
            return supportChatRoomRepository.findFirstByAgentEqualsOrderByCreationDateDesc(sender);
        }
        return null;
    }

    void addMessageToHistory(SupportChatRoom chatRoom, Message message) {
        List<Message> messageHistory = chatRoom.getMessageHistory();

        if (messageHistory == null) {
            messageHistory = new ArrayList<>();
        }

        messageHistory.add(message);
        supportChatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void closeChatBy(User user, Message leaveMessage) {
        Role.Type roleType = user.getCurrentRoleType();
        SupportChatRoom currentRoom  = getLastChatRoom(user, roleType);


        if(currentRoom == null) {
            generalSender.send(messageService.createInfoMessage(user, "You aren't chatting."));
            return;
        }
        SupportChatRoom.State currentState = currentRoom.getState();

        if(currentState == SupportChatRoom.State.CLOSED) {
            generalSender.send(messageService.createInfoMessage(user, "You aren't chatting."));
        } else if(currentState == SupportChatRoom.State.NEED_CLIENT
                || currentState == SupportChatRoom.State.NEED_AGENT) {
            addMessageToHistory(currentRoom, leaveMessage);
            Message info = messageService.createInfoMessage(user, "You haven't companion. You can't leave.");
            addMessageToHistory(currentRoom, info);
            generalSender.send(info);
        } else if(currentState == SupportChatRoom.State.FULL){
            addMessageToHistory(currentRoom, leaveMessage);
            User companion = currentRoom.getCompanionFor(roleType);
            Message info = messageService.createInfoMessage(user, currentRoom,
                    "You left chat with "
                            + companion.getCurrentRoleType().toString().toLowerCase()
                            + " " + companion.getName());
            generalSender.send(info);
            Message infoForCompanion = messageService.createInfoMessage(companion, currentRoom,
                    "Your companion has left the chat.");
            generalSender.send(infoForCompanion);

            currentRoom.setState(SupportChatRoom.State.CLOSED);
            supportChatRoomRepository.save(currentRoom);

            User agent = currentRoom.getAgent();
            User client = currentRoom.getClient();

            directUserToChat(agent);
            generalSender.send(messageService.createInfoMessage(client,
                    "Type your messages and we will find you an agent."));

        }
    }
}
