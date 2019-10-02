package com.olrox.chat.controller.chat.handler;

import com.olrox.chat.controller.chat.util.MessageParser;
import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.SupportChatRoomService;
import com.olrox.chat.service.UserService;
import com.olrox.chat.service.sending.GeneralSender;
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
    private MessageService messageService;

    @Autowired
    private GeneralSender generalSender;

    @Autowired
    private MessageParser messageParser;

    @Override
    public void handleCommand(User user, String data) {
        messageService.createUserMessage(user, data, MessageType.USER_TO_SERVER);

        if (user.isRegistered()) {
            Message message = messageService.createInfoMessage(user,
                    "You are already registered as " + user.getName());
            generalSender.send(message);

            return;
        }

        String[] params;

        try {
            params = messageParser.parseRegisterMessage(data);
        } catch (Exception ex) {
            Message errorMessage = messageService.createErrorMessage(user,
                    "Wrong command syntax " + data);
            generalSender.send(errorMessage);
            return;
        }

        Role.Type role;

        try {
            role = Role.Type.valueOf(params[0].toUpperCase());
        } catch (IllegalArgumentException ex) {
            Message errorMessage = messageService.createErrorMessage(user, "Wrong role " + params[0]);
            generalSender.send(errorMessage);
            return;
        }

        String name = params[1];

        if (name == null || name.isEmpty()) {
            Message errorMessage = messageService.createErrorMessage(user,
                    "You forget to enter your name");
            generalSender.send(errorMessage);
            return;
        }

        user.setName(name);
        userService.register(user, role);
    }

    @Override
    public boolean checkMatch(String data) {
        return data.matches(regex);
    }
}
