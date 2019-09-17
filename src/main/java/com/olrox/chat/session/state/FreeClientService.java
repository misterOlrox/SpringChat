package com.olrox.chat.session.state;

import com.olrox.chat.entity.Message;
import com.olrox.chat.exception.InvalidUserStateException;
import com.olrox.chat.service.UserService;
import com.olrox.chat.session.ChatSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FreeClientService implements ChatService {

    private final static Logger LOGGER = LogManager.getLogger(FreeClientService.class);

    private ChatSession chatSession;
    private List<Message> messages = new ArrayList<>();
    private boolean isWaiting = false;

    private UserService userService;

    public FreeClientService() {
    }

    public FreeClientService(UnauthorizedService previousState) {
        this.chatSession = previousState.getChatSession();
        this.chatSession.receiveFromServer("Type your messages and we will find you an agent.");
    }

    public FreeClientService(BusyClientService busyClientState) {
        this.chatSession = busyClientState.getChatSession();
    }

    public FreeClientService(ChatSession chatSession) {
        this.chatSession = chatSession;
    }

    @Autowired
    public FreeClientService(UserService userService) {
        this.userService = userService;
    }

    private void findCompanion() {
        ChatSession companion = userService.pollFreeAgent();
        if (companion != null) {
            connect(companion);
        } else {
            chatSession.receiveFromServer("We haven't free agents. You can write messages and they will be saved.");
            userService.addFreeClient(this.chatSession);
            isWaiting = true;
        }
    }

    private void connect(ChatSession companion) {
        ChatService companionState = companion.getState();
        if (companionState instanceof FreeAgentService) {
            BusyClientService busyClient = new BusyClientService(this);
            BusyAgentService busyAgent = new BusyAgentService((FreeAgentService) companionState);

            busyClient.setCompanion(busyAgent);
            busyAgent.setCompanion(busyClient);

            this.chatSession.setState(busyClient);
            companion.setState(busyAgent);

            chatSession.receiveFromServer("Now you chatting with agent " + companion.getUserName());
            companion.receiveFromServer("Now you chatting with client " + this.chatSession.getUserName());

            LOGGER.info("Client " + this.chatSession.getUserName() + " start chat with agent " + companion.getUserName());

            for (Message message : messages) {
                busyAgent.receiveFromClient(message);
            }
        } else {
            throw new InvalidUserStateException("Companion isn't in FreeAgentState.");
        }
    }

    @Override
    public void register(Message message) {
        chatSession.receiveFromServer("You are already registered as client " + chatSession.getUserName());
    }

    @Override
    public void sendMessage(Message message) {
        messages.add(message);
        if (!isWaiting) {
            findCompanion();
        }
    }

    @Override
    public void leave() {
        chatSession.receiveFromServer("You aren't chatting.");
    }

    @Override
    public void exit() {
        userService.removeOnlineUser(chatSession.getUserName());
    }

    public ChatSession getChatSession() {
        return chatSession;
    }

    public List<Message> getMessages() {
        return messages;
    }
}