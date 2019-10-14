package com.olrox.chat.service;

import com.google.common.collect.ImmutableMap;
import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.SupportChatRoom;
import com.olrox.chat.entity.User;
import com.olrox.chat.exception.AlreadySignedInException;
import com.olrox.chat.exception.AuthenticationException;
import com.olrox.chat.exception.EmptyNameException;
import com.olrox.chat.exception.EmptyPasswordException;
import com.olrox.chat.exception.NameIsBusyException;
import com.olrox.chat.exception.UserNotFoundException;
import com.olrox.chat.repository.RoleRepository;
import com.olrox.chat.repository.UserRepository;
import com.olrox.chat.service.sending.GeneralSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final MessageService messageService;
    private final GeneralSender generalSender;
    private final SupportChatRoomService supportChatRoomService;
    private final PasswordEncoder passwordEncoder;
    private final JWTTokenService tokenService;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository,
                       MessageService messageService,
                       GeneralSender generalSender,
                       SupportChatRoomService supportChatRoomService,
                       @Lazy PasswordEncoder passwordEncoder,
                       JWTTokenService tokenService,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.messageService = messageService;
        this.generalSender = generalSender;
        this.supportChatRoomService = supportChatRoomService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.roleRepository = roleRepository;
    }

    public User addUnauthorizedUser(ConnectionType connectionType) {
        User newUser = new User();
        newUser.setConnectionType(connectionType);
        newUser.setCurrentRoleType(Role.Type.UNKNOWN);

        User savedUser = userRepository.save(newUser);

        return savedUser;
    }

    public User register(User user, Role.Type role, String name, String password) {
        if (user.isRegistered()) {
            throw new AlreadySignedInException("User " + user.getName() + "already signed in");
        }
        if (name == null || name.isEmpty()) {
            throw new EmptyNameException();
        }
        if (userRepository.findByName(name).isPresent()) {
            throw new NameIsBusyException("User with name " + name + " already exists.");
        }
        if (password == null || password.isEmpty()) {
            throw new EmptyPasswordException("Password can't be empty.");
        }

        user.setCurrentRoleType(role);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user = userRepository.save(user);

        Message message = messageService.createInfoMessage(user,
                "You are successfully registered as "
                        + user.getCurrentRoleType().name().toLowerCase() + " " + user.getName());
        generalSender.send(message);

        if (role.equals(Role.Type.AGENT)) {
            supportChatRoomService.directUserToChat(user);
            LOGGER.info("Agent " + user.getName() + " registered.");
        } else if (role.equals(Role.Type.CLIENT)) {
            generalSender.send(messageService.createInfoMessage(user,
                    "Type your messages and we will find you an agent."));
            LOGGER.info("Client " + user.getName() + " registered.");
        }

        return user;
    }

    @Transactional
    public void handleExit(User user) {
        SupportChatRoom currentRoom = supportChatRoomService.getLastChatRoom(user, user.getCurrentRoleType());
        user.setConnectionType(ConnectionType.OFFLINE);
        user.setCurrentRoleType(Role.Type.UNKNOWN);
        userRepository.save(user);
        supportChatRoomService.closeChat(user, currentRoom);
        messageService.createInfoMessage(user, "You exit from our app.");
        LOGGER.info("User " + user.getName() + " logged out.");
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found " + username));
    }

    public String getTokenForRegistered(User user) {
        return tokenService.expiring(ImmutableMap.of("username", user.getUsername()));
    }

    public Optional<User> findByToken(String token) {
        return Optional
                .of(tokenService.verify(token))
                .map(map -> map.get("username"))
                .flatMap(userRepository::findByName)
                .filter((x) -> x.getCurrentRoleType() != Role.Type.UNKNOWN);
    }

    public User login(String name, String password) {
        if (name == null || password == null) {
            throw new AuthenticationException("Username or password is incorrect.");
        }

        User user = userRepository.findByName(name)
                .orElseThrow(() -> new AuthenticationException("Username or password is incorrect."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Username or password is incorrect.");
        }

        if (user.getCurrentRoleType() != Role.Type.UNKNOWN) {
            throw new AlreadySignedInException("This user is already signed in.");
        }

        Role.Type role = roleRepository.findFirstByUserEqualsOrderByChatRoomDesc(user).getType();
        user.setCurrentRoleType(role);
        user = userRepository.save(user);

        Message message = messageService.createInfoMessage(user,
                "You are successfully logged in as "
                        + user.getCurrentRoleType().name().toLowerCase() + " " + user.getName());
        generalSender.send(message);

        if (role.equals(Role.Type.AGENT)) {
            supportChatRoomService.directUserToChat(user);
            LOGGER.info("Agent " + user.getName() + " logged in.");
        } else if (role.equals(Role.Type.CLIENT)) {
            generalSender.send(messageService.createInfoMessage(user,
                    "Type your messages and we will find you an agent."));
            LOGGER.info("Client " + user.getName() + " logged in.");
        }

        return user;
    }
}
