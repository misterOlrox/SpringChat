package com.olrox.chat.service;

import com.google.common.collect.ImmutableMap;
import com.olrox.chat.entity.*;
import com.olrox.chat.exception.AlreadyRegisteredException;
import com.olrox.chat.exception.EmptyPasswordException;
import com.olrox.chat.exception.NameIsBusyException;
import com.olrox.chat.exception.UserNotFoundException;
import com.olrox.chat.exception.EmptyNameException;
import com.olrox.chat.repository.UserRepository;
import com.olrox.chat.service.sending.GeneralSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private GeneralSender generalSender;

    @Autowired
    private SupportChatRoomService supportChatRoomService;

    public User addUnauthorizedUser(ConnectionType connectionType) {
        User newUser = new User();
        newUser.setConnectionType(connectionType);
        newUser.setCurrentRoleType(Role.Type.UNKNOWN);

        User savedUser = userRepository.save(newUser);

        return savedUser;
    }

    public User register(User user, Role.Type role, String name, String password) {
        if (user.isRegistered()) {
            throw new AlreadyRegisteredException(user);
        }
        if (name == null || name.isEmpty()) {
            throw new EmptyNameException(user);
        }
        if(userRepository.findByName(name).isPresent()) {
            throw new NameIsBusyException("User with name " + name + " already exists.");
        }
        if(password == null || password.isEmpty()) {
            throw new EmptyPasswordException("Password can't be empty.");
        }

        user.setCurrentRoleType(role);
        user.setName(name);
        user.setPassword(password);
        user = userRepository.save(user);

        Message message = messageService.createInfoMessage(user,
                "You are successfully registered as "
                        + user.getCurrentRoleType().name().toLowerCase() + " " + user.getName());
        generalSender.send(message);

        if (role.equals(Role.Type.AGENT)) {
            supportChatRoomService.directUserToChat(user);
        } else if (role.equals(Role.Type.CLIENT)) {
            generalSender.send(messageService.createInfoMessage(user,
                    "Type your messages and we will find you an agent."));
        }

        return user;
    }

    @Transactional
    public void handleExit(User user) {
        // TODO disconnect online users if invoked from rest
        SupportChatRoom currentRoom = supportChatRoomService.getLastChatRoom(user, user.getCurrentRoleType());
        user.setConnectionType(ConnectionType.OFFLINE);
        user.setCurrentRoleType(Role.Type.UNKNOWN);
        userRepository.save(user);
        supportChatRoomService.closeChat(user, currentRoom);
    }

    public User getUserById(Long id) {
        Optional<User> optionalValue = userRepository.findById(id);

        return optionalValue.orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));
    }

    public User getAgent(long id) {
        Optional<User> optionalValue = userRepository.findFirstByIdAndCurrentRoleType(id, Role.Type.AGENT);

        return optionalValue.orElseThrow(() -> new UserNotFoundException("Agent with id " + id + " doesn't exist."));
    }

    public User getClient(long id) {
        Optional<User> optionalValue = userRepository.findFirstByIdAndCurrentRoleType(id, Role.Type.CLIENT);

        return optionalValue.orElseThrow(() -> new UserNotFoundException("Client with id " + id + " doesn't exist."));
    }

    public List<User> getAllAgents() {
        return userRepository.findAllByCurrentRoleTypeEquals(Role.Type.AGENT);
    }

    public Page<User> getAllAgentsPage(Pageable pageable) {
        return userRepository.findAllByCurrentRoleTypeEquals(Role.Type.AGENT, pageable);
    }

    public Page<User> getFreeAgents(Pageable pageable) {
        return userRepository.findFreeAgents(pageable);
    }

    public Long countFreeAgents() {
        return userRepository.countFreeAgents();
    }

    public void sendFirstMessages(User user) {
        generalSender.send(messageService.createGreetingMessage(user));
        generalSender.send(messageService.createRegisterInfoMessage(user));
    }

    public Page<User> getClientQueue(Pageable pageable) {
        return userRepository.findClientsInQueue(pageable);
    }


    @Autowired
    private JWTTokenService tokenService;


    public String getTokenForRegistered(User user) {
        return tokenService.expiring(ImmutableMap.of("username", user.getUsername()));
    }

    public Optional<User> findByToken(String token) {
        return Optional
                .of(tokenService.verify(token))
                .map(map -> map.get("username"))
                .flatMap(userRepository::findByName);
    }
}
