package com.olrox.chat.session.state;

import com.olrox.chat.entity.Message;
import com.olrox.chat.exception.InvalidUserStateException;
import com.olrox.chat.service.UserService;
import com.olrox.chat.session.ChatSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FreeAgentService implements ChatService {

    private final static Logger LOGGER = LogManager.getLogger(FreeAgentService.class);

    private ChatSession chatSession;

    private UserService userService;

    public FreeAgentService() {
    }

    public FreeAgentService(UnauthorizedService user){
        this.chatSession = user.getChatSession();
    }

    public FreeAgentService(BusyAgentService busyAgent) {
        this.chatSession = busyAgent.getChatSession();
    }

    public FreeAgentService(ChatSession chatSession) {
        this.chatSession = chatSession;
    }

    @Autowired
    public FreeAgentService(UserService userService) {
        this.userService = userService;
    }

    public synchronized void findCompanion(){
        LOGGER.debug("Agent " + this.chatSession.getUserName() + " trying to find client");
        ChatSession companion = userService.pollFreeClient();
        if(companion != null){
            connect(companion);
        } else {
            chatSession.receiveFromServer("Wait for your companion.");
            userService.addFreeAgent(this.chatSession);
        }
    }

    private void connect(ChatSession companion){
        FreeClientService companionState;
        if(companion.getState() instanceof FreeClientService) {
            companionState = (FreeClientService) companion.getState();
        } else {
            throw new InvalidUserStateException("Companion isn't in FreeClientState.");
        }

        BusyClientService busyClient = new BusyClientService(companionState);
        BusyAgentService busyAgent = new BusyAgentService(this);

        busyClient.setCompanion(busyAgent);
        busyAgent.setCompanion(busyClient);

        chatSession.setState(busyAgent);
        companion.setState(busyClient);

        chatSession.receiveFromServer("Now you chatting with client " + companion.getUserName());
        companion.receiveFromServer("Now you chatting with agent " + this.getChatSession().getUserName());

        LOGGER.info("Agent " + this.chatSession.getUserName() + " start chat with client " + companion.getUserName());

        for (Message message : companionState.getMessages()) {
            busyAgent.receiveFromClient(message);
        }
    }

    @Override
    public void register(Message message) {
        chatSession.receiveFromServer("You are already registered as agent " + this.chatSession.getUserName());
    }

    @Override
    public void sendMessage(Message message) {
        chatSession.receiveFromServer("You haven't companion. Your message will not be delivered.");
    }

    @Override
    public void leave() {
        chatSession.receiveFromServer("You are agent. You can't leave.");
    }

    @Override
    public void exit() {
        userService.removeOnlineUser(this.chatSession.getUserName());
    }

    public ChatSession getChatSession() {
        return chatSession;
    }
}
