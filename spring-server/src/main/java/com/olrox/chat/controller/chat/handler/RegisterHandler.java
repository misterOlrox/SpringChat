package com.olrox.chat.controller.chat.handler;

import com.olrox.chat.controller.chat.util.RegisterCommandParser;
import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.MessageService;
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

    @Override
    public boolean checkMatch(String data) {
        return data.matches(regex);
    }

    @Override
    public void handleCommand(User user, String data) {
        messageService.createUserMessage(user, data, MessageType.USER_TO_SERVER);

        if (user.isRegistered()) {
            sendAlreadyRegisteredMessage(user);
            return;
        }

        RegisterCommandParser parser = new RegisterCommandParser();
        parser.parse(data);

        String parsedRole = parser.getParsedRole();
        String parsedName = parser.getParsedName();

        Role.Type role;
        try {
            role = Role.Type.valueOf(parsedRole.toUpperCase());
        } catch (IllegalArgumentException ex) {
            sendWrongRoleMessage(user, parsedRole);
            return;
        }

        if (parsedName == null || parsedName.isEmpty()) {
            sendEmptyNameMessage(user);
            return;
        }

        userService.register(user, parsedName, role);
    }

    private void sendAlreadyRegisteredMessage(User user) {
        Message message = messageService.createInfoMessage(user,
                "You are already registered as "
                        + user.getCurrentRoleType().toString().toLowerCase()
                        + " " + user.getName());
        generalSender.send(message);
    }

    private void sendEmptyNameMessage(User user) {
        Message errorMessage = messageService.createErrorMessage(user,
                "You forget to enter your name");
        generalSender.send(errorMessage);
        Message register = messageService.createRegisterInfoMessage(user);
        generalSender.send(register);
    }

    private void sendWrongRoleMessage(User user, String parsedRole) {
        Message errorMessage = messageService.createErrorMessage(user, "Wrong role " + parsedRole);
        generalSender.send(errorMessage);
        Message register = messageService.createRegisterInfoMessage(user);
        generalSender.send(register);
    }
}
