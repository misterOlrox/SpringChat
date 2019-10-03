package com.olrox.chat.controller.chat.handler;

import com.olrox.chat.controller.chat.util.RegisterCommandParser;
import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.User;
import com.olrox.chat.exception.EmptyNameException;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.UserService;
import com.olrox.chat.service.sending.GeneralSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Controller
@Order(value = 1)
public class RegisterHandler implements CommandHandler{

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
            Message errorMessage = messageService.createErrorMessage(user, "Wrong role " + parsedRole);
            generalSender.send(errorMessage);
            return;
        }

        userService.register(user, parsedName, role);
    }

    public void sendAlreadyRegisteredMessage(User user) {
        Message message = messageService.createInfoMessage(user,
                "You are already registered as "
                        + user.getCurrentRoleType().toString().toLowerCase()
                        + " " + user.getName());
        generalSender.send(message);
    }

    @ExceptionHandler(EmptyNameException.class)
    public void handleException(EmptyNameException exception) {
        User user = exception.getUser();

        Message errorMessage = messageService.createErrorMessage(user,
                "You forget to enter your name");
        generalSender.send(errorMessage);
    }
}
