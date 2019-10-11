package com.olrox.chat.rest;

import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthRestController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password,
            @RequestParam("role") final Role.Type role) {

        User user = new User();
        user.setConnectionType(ConnectionType.OFFLINE);
        user = userService.register(user, role, username, password);

        return userService.getTokenForRegistered(user);
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("username") final String username,
            @RequestParam("password") final String password) {

        User user = userService.login(username, password);

        return userService.getTokenForRegistered(user);
    }
}
