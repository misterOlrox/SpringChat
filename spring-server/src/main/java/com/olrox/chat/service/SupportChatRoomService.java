package com.olrox.chat.service;

import com.olrox.chat.entity.*;
import com.olrox.chat.repository.SupportChatRoomRepository;
import com.olrox.chat.repository.UserRepository;
import com.olrox.chat.service.sending.GeneralSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SupportChatRoomService {

    private final SupportChatRoomRepository supportChatRoomRepository;
    private final UserRepository userRepository;
    private final GeneralSender generalSender;
    private final RoleService roleService;
    private final MessageService messageService;
    private final MessageDetailService messageDetailService;

    @Autowired
    public SupportChatRoomService(SupportChatRoomRepository supportChatRoomRepository,
                                  UserRepository userRepository,
                                  GeneralSender generalSender,
                                  RoleService roleService,
                                  MessageService messageService,
                                  MessageDetailService messageDetailService) {
        this.supportChatRoomRepository = supportChatRoomRepository;
        this.userRepository = userRepository;
        this.generalSender = generalSender;
        this.roleService = roleService;
        this.messageService = messageService;
        this.messageDetailService = messageDetailService;
    }

    private SupportChatRoom createNewChat(User creator) {
        Role.Type roleType = creator.getCurrentRoleType();
        SupportChatRoom dialogue = new SupportChatRoom();

        if (roleType == Role.Type.AGENT) {
            dialogue.setState(SupportChatRoom.State.NEED_CLIENT);
            dialogue.setAgent(creator);
        } else if (roleType == Role.Type.CLIENT) {
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

        Role.Type roleInChat = user.getCurrentRoleType();

        roleService.create(chat, user, roleInChat);

        if (roleInChat == Role.Type.AGENT) {
            chat.setAgent(user);
            notifyAboutNewCompanion(chat);
            sendMessagesToNewAgent(user, chat.getMessageHistory());
        } else if (roleInChat == Role.Type.CLIENT) {
            chat.setClient(user);
            notifyAboutNewCompanion(chat);
        }

        chat.setState(SupportChatRoom.State.FULL);
        supportChatRoomRepository.save(chat);
    }

    private void sendMessagesToNewAgent(User newAgent, List<Message> messageHistory) {
        for (Message message : messageHistory) {
            if (message.getType() == MessageType.USER_TO_CHAT) {
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

        SupportChatRoom currentRoom = getLastChatRoom(sender, senderRole);

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
            User recipient = currentRoom.getCompanionFor(sender);
            messageDetailService.create(message,
                    recipient,
                    MessageDetail.Status.NOT_RECEIVED);
            generalSender.send(message);
        }
    }

    public SupportChatRoom getLastChatRoom(User sender, Role.Type senderRole) {
        if (senderRole == Role.Type.CLIENT) {
            return supportChatRoomRepository.findFirstByClientEqualsOrderByCreationDateDesc(sender);
        } else if (senderRole == Role.Type.AGENT) {
            return supportChatRoomRepository.findFirstByAgentEqualsOrderByCreationDateDesc(sender);
        }
        return null;
    }

    @Transactional
    public void addMessageToHistory(SupportChatRoom chatRoom, Message message) {
        List<Message> messageHistory = chatRoom.getMessageHistory();

        if (messageHistory == null) {
            chatRoom.setMessageHistory(new ArrayList<>());
        }

        chatRoom.getMessageHistory().add(message);
        supportChatRoomRepository.save(chatRoom);
    }

    @Transactional
    public void leaveChat(User user, Message leaveMessage) {
        SupportChatRoom currentRoom = getLastChatRoom(user, user.getCurrentRoleType());

        if (currentRoom == null) {
            generalSender.send(messageService.createInfoMessage(user, "You aren't chatting."));
            return;
        }
        SupportChatRoom.State currentState = currentRoom.getState();

        if (currentState == SupportChatRoom.State.CLOSED) {
            generalSender.send(messageService.createInfoMessage(user, "You aren't chatting."));
        } else if (currentState == SupportChatRoom.State.NEED_CLIENT
                || currentState == SupportChatRoom.State.NEED_AGENT) {
            addMessageToHistory(currentRoom, leaveMessage);
            Message info = messageService.createInfoMessage(user, "You haven't companion. You can't leave.");
            addMessageToHistory(currentRoom, info);
            generalSender.send(info);
        } else if (currentState == SupportChatRoom.State.FULL) {
            addMessageToHistory(currentRoom, leaveMessage);

            User companion = currentRoom.getCompanionFor(user);
            Message info = messageService.createInfoMessage(user, currentRoom, "You left chat with "
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

    public void closeChat(User initiator, SupportChatRoom currentRoom) {
        if (currentRoom == null) {
            return;
        }
        SupportChatRoom.State previousState = currentRoom.getState();

        currentRoom.setState(SupportChatRoom.State.CLOSED);

        if (previousState == SupportChatRoom.State.FULL) {

            User companion = currentRoom.getCompanionFor(initiator);

            if (companion != null) {
                Message infoForCompanion = messageService.createInfoMessage(companion, currentRoom,
                        "Your companion has left the chat.");
                generalSender.send(infoForCompanion);
            }

            supportChatRoomRepository.save(currentRoom);

            User agent = currentRoom.getAgent();
            User client = currentRoom.getClient();

            if (agent != null) {
                directUserToChat(agent);
            }
            if (client != null) {
                generalSender.send(messageService.createInfoMessage(client,
                        "Type your messages and we will find you an agent."));
            }
        }
    }

    public Page<SupportChatRoom> getOpenChats(Pageable pageable) {
        return supportChatRoomRepository.findAllByStateIsNot(SupportChatRoom.State.CLOSED, pageable);
    }
}
