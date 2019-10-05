package com.olrox.chat.service;

import com.olrox.chat.entity.*;
import com.olrox.chat.exception.AlreadyRegisteredException;
import com.olrox.chat.exception.UserNotFoundException;
import com.olrox.chat.exception.EmptyNameException;
import com.olrox.chat.repository.UserRepository;
import com.olrox.chat.service.sending.GeneralSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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

        User savedUser = userRepository.save(newUser);

        return savedUser;
    }

    public User getUserById(long id) {
        Optional<User> optionalValue = userRepository.findById(id);

        return optionalValue.orElseThrow(() -> new UserNotFoundException("User doesn't exist."));
    }

    public User register(User user, String name, Role.Type role) {
        if (user.isRegistered()) {
            throw new AlreadyRegisteredException(user);
        }
        if (name == null || name.isEmpty()) {
            throw new EmptyNameException(user);
        }

        user.setCurrentRoleType(role);
        user.setName(name);
        user = userRepository.save(user);

        Message message = messageService.createInfoMessage(user,
                "You are successfully registered as "
                        + user.getCurrentRoleType().name().toLowerCase() + " " + user.getName());
        generalSender.send(message);

        if(role.equals(Role.Type.AGENT)) {
            supportChatRoomService.directUserToChat(user);
        } else if(role.equals(Role.Type.CLIENT)) {
            generalSender.send(messageService.createInfoMessage(user,
                    "Type your messages and we will find you an agent."));
        }

        return user;
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
}
