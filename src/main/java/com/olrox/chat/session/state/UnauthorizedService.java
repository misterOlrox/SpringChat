package com.olrox.chat.session.state;

import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.User;
import com.olrox.chat.message.author.AuthorType;
import com.olrox.chat.service.UserService;
import com.olrox.chat.session.ChatSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.StringTokenizer;

@Component
public class UnauthorizedService implements ChatService {

    private final static Logger LOGGER = LogManager.getLogger(UnauthorizedService.class);

    private UserService userService;

    private ChatSession chatSession;

    public UnauthorizedService(){}

    public UnauthorizedService(ChatSession chatSession) {
        this.chatSession = chatSession;

        chatSession.receiveFromServer("Hello ಠ_ಠ");
        writeOptions();
    }

    @Autowired
    public UnauthorizedService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void register(Message message) {
        String response = message.getText();
        StringTokenizer tokenizer = new StringTokenizer(response, " ");

        if(tokenizer.countTokens() != 3) {
            chatSession.receiveFromServer("Incorrect command.");
            return;
        }

        tokenizer.nextToken();
        String userType =  tokenizer.nextToken();
        String username = tokenizer.nextToken();

        if(!userType.equals("agent") && !userType.equals("client")){
            chatSession.receiveFromServer("Sorry. You can't register as " + userType + ". Try again");
            return;
        }

        boolean isNewUser = userService.addOnlineUser(username);

        if(!isNewUser){
            chatSession.receiveFromServer("User with username " + username + " already exists.");
            return;
        }

        String serverAnswer = "You are successfully registered as "
                + userType + " " + username;
        String loggerInfo = "User was registered as "
                + userType + " " + username;

        User user = new User(username, AuthorType.valueOf(userType.toUpperCase()));

        chatSession.setUser(user);
        chatSession.receiveFromServer(serverAnswer);
        LOGGER.info(loggerInfo);

        if(userType.equals("agent")){
            FreeAgentService state = new FreeAgentService(this);
            chatSession.setState(state);
            state.findCompanion();
        }
        if(userType.equals("client")){
            chatSession.setState(new FreeClientService(this));
        }
    }

    @Override
    public void sendMessage(Message message) {
        writeOptions();
    }

    @Override
    public void leave() {
        writeOptions();
    }

    @Override
    public void exit() {
        // do nothing
    }

    private void writeOptions() {
        chatSession.receiveFromServer("Print \"/register [agent|client] YourName\" to register");
    }

    public ChatSession getChatSession() {
        return chatSession;
    }
}