package com.olrox.chat.service;

import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.User;
import com.olrox.chat.repository.UserRepository;
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

    public User addUnauthorizedUser(ConnectionType connectionType) {
        User newUser = new User();
        newUser.setConnectionType(connectionType);

        User savedUser = userRepository.save(newUser);

        return savedUser;
    }

    public User getUserById(long id) {
        Optional<User> optionalValue = userRepository.findById(id);

        return optionalValue.orElseThrow(() -> new RuntimeException("User doesn't exist."));
    }

    public User register(User user, String name, Role.Type roleType) {
        user.setName(name);
        user.setCurrentRoleType(roleType);

        return userRepository.save(user);
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
}
