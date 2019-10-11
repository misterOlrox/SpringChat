package com.olrox.chat.chatcontroller.handler;

import com.olrox.chat.chatcontroller.util.LoginCommandParser;
import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.ConnectionService;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.UserService;
import com.olrox.chat.service.sending.GeneralSender;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(value = 2)
public class LoginHandler implements CommandHandler{
    private final static String regex = "/login .+";

    private final MessageService messageService;
    private final UserService userService;
    private final ConnectionService connectionService;
    private final GeneralSender generalSender;

    public LoginHandler(MessageService messageService,
                        UserService userService,
                        ConnectionService connectionService,
                        GeneralSender generalSender) {
        this.messageService = messageService;
        this.userService = userService;
        this.connectionService = connectionService;
        this.generalSender = generalSender;
    }

    @Override
    public void handleCommand(User user, String data) {
        // Message message = messageService.createUserMessage(user, data, MessageType.USER_TO_SERVER);
        LoginCommandParser commandParser = new LoginCommandParser();
        commandParser.parse(data);
        String parsedName = commandParser.getParsedName();
        String parsedPassword = commandParser.getParsedPassword();

        User loggedUser = user;
        try {
            loggedUser = userService.login(parsedName, parsedPassword);
        } catch (RuntimeException ex) {
            Message errorMessage = messageService.createErrorMessage(user,
                    ex.getMessage());
            generalSender.send(errorMessage);
            Message register = messageService.createRegisterInfoMessage(user);
            generalSender.send(register);

            return;
        }
        connectionService.replaceConnection(user, loggedUser);

        generalSender.resendAllFor(loggedUser);
    }

    @Override
    public boolean checkMatch(String data) {
        return data.matches(regex);
    }
}
