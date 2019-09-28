package com.olrox.chat.controller;

import com.olrox.chat.config.CustomSpringConfigurator;
import com.olrox.chat.controller.util.ChatCommand;
import com.olrox.chat.controller.util.MessageParser;
import com.olrox.chat.entity.*;
import com.olrox.chat.service.ChatRoomService;
import com.olrox.chat.service.ConnectionService;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.UserService;
import com.olrox.chat.service.sending.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chat", configurator = CustomSpringConfigurator.class)
public class ChatWebSocketEndpoint {

    private final Map<Session, Long> sessions = new ConcurrentHashMap<>();

    @Autowired
    private ConnectionService connectionService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private MessageService messageService;

    @Autowired
    @Qualifier(ConnectionType.TypeConstants.WEBSOCKET_SENDER)
    private MessageSender messageSender;

    @Autowired
    private MessageParser messageParser;

    @OnOpen
    public void onOpen(Session session) {
        User user = userService.addUnauthorizedUser(ConnectionType.WEBSOCKET);
        long userId = user.getId();
        sessions.put(session, userId);
        connectionService.addWebSocketSession(userId, session);

        messageSender.send(messageService.createGreetingMessage(user));
        messageSender.send(messageService.createRegisterInfoMessage(user));
    }

    @OnClose
    public void onClose(Session session) {

    }

    @OnMessage
    public void onMessage(Session session, String text) {
        Long userId = sessions.get(session);
        User user = userService.getUserById(userId);
        Message message = messageService.createUserMessage(user, text, MessageType.USER_TO_SERVER);

        for (ChatCommand chatCommand : chatCommands) {
            if (chatCommand.checkMatch(text)) {
                chatCommand.execute(user, text);
                return;
            }
        }
    }

    private final ChatCommand REGISTER = new ChatCommand("\\/register .+") {
        @Override
        public void execute(User user, String text) {
            if(user.isRegistered()) {
                Message message = messageService.createInfoMessage(user,
                        "You are already registered as " + user.getName());
                messageSender.send(message);

                return;
            }

            String[] params;

            try {
                params = messageParser.parseRegisterMessage(text);
            } catch (Exception ex) {
                Message errorMessage = messageService.createErrorMessage(user,
                        "Wrong command syntax " + text);
                messageSender.send(errorMessage);
                return;
            }

            Role.Type role;

            try {
                role = Role.Type.valueOf(params[0].toUpperCase());
            } catch (IllegalArgumentException ex) {
                Message errorMessage = messageService.createErrorMessage(user, "Wrong role " + params[0]);
                messageSender.send(errorMessage);
                return;
            }

            String name = params[1];

            if(name == null || name.isEmpty()) {
                Message errorMessage = messageService.createErrorMessage(user,
                        "You forger to enter your name");
                messageSender.send(errorMessage);
                return;
            }

            userService.register(user, name);

            chatRoomService.createNewClientAgentDialogue(user, role);

            Message message = messageService.createInfoMessage(user,
                    "You are successfully registered as " + user.getName());
            messageSender.send(message);
        }
    };

    private final ChatCommand LEAVE = new ChatCommand("\\/leave") {
        @Override
        public void execute(User user, String text) {

        }
    };

    private final ChatCommand EXIT = new ChatCommand("\\/exit") {
        @Override
        public void execute(User user, String text) {

        }
    };

    private final ChatCommand SEND_MESSAGE = new ChatCommand(".*") {
        @Override
        public void execute(User user, String text) {
            if(!user.isRegistered()) {
                Message message = messageService.createRegisterInfoMessage(user);
                messageSender.send(message);
            }
            else if(user.getChatRooms() == null || user.getChatRooms().isEmpty()) {
                Message message = messageService.createInfoMessage(user, "You aren't chatting. Your message will not be delivered.");
                messageSender.send(message);
            }


        }
    };

    private final ChatCommand[] chatCommands = new ChatCommand[]{
            REGISTER,
            LEAVE,
            EXIT,
            SEND_MESSAGE
    };
}
