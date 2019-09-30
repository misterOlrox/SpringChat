package com.olrox.chat.controller.handler;

import com.olrox.chat.controller.util.MessageParser;
import com.olrox.chat.entity.*;
import com.olrox.chat.service.SupportChatRoomService;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.UserService;
import com.olrox.chat.service.sending.MessageSender;
import com.olrox.chat.service.sending.MessageSenderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class RegisterHandler implements CommandHandler {

    private final static String regex = "/register .+";

    @Autowired
    private UserService userService;

    @Autowired
    private SupportChatRoomService supportChatRoomService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageSenderFactory messageSenderFactory;

    @Autowired
    private MessageParser messageParser;

    @Override
    public void handleCommand(User user, String data) {
        messageService.createUserMessage(user, data, MessageType.USER_TO_SERVER);

        MessageSender messageSender = messageSenderFactory.getMessageSender(user.getConnectionType());

        if (user.isRegistered()) {
            Message message = messageService.createInfoMessage(user,
                    "You are already registered as " + user.getName());
            messageSender.send(message);

            return;
        }

        String[] params;

        try {
            params = messageParser.parseRegisterMessage(data);
        } catch (Exception ex) {
            Message errorMessage = messageService.createErrorMessage(user,
                    "Wrong command syntax " + data);
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

        if (name == null || name.isEmpty()) {
            Message errorMessage = messageService.createErrorMessage(user,
                    "You forget to enter your name");
            messageSender.send(errorMessage);
            return;
        }

        userService.register(user, name, role);

        Message message = messageService.createInfoMessage(user,
                "You are successfully registered as " + user.getName());
        messageSender.send(message);

        if(role.equals(Role.Type.AGENT)) {
            supportChatRoomService.directUserToChat(user, role);
        } else if(role.equals(Role.Type.CLIENT)) {
            messageSender.send(messageService.createInfoMessage(user,
                    "Type your messages and we will find you an agent."));
        }
    }

    @Override
    public boolean checkMatch(String data) {
        return data.matches(regex);
    }
}
