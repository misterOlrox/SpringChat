package com.olrox.chat.rest;

import com.olrox.chat.dto.DetailedUserDto;
import com.olrox.chat.dto.UserRegisterDto;
import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/register")
public class RegisterRestController {
    @Autowired
    private UserService userService;

    @PostMapping("")
    public String register(@RequestBody UserRegisterDto userRegisterDto) {
        com.olrox.chat.entity.User user = new com.olrox.chat.entity.User();
        user.setConnectionType(ConnectionType.OFFLINE);
        user = userService.register(user,
                userRegisterDto.getRole(),
                userRegisterDto.getName(),
                userRegisterDto.getPassword());

        return userService.getTokenForRegistered(user);
    }

//    @PostMapping("/login")
//    String login(
//            @RequestParam("username") final String username,
//            @RequestParam("password") final String password) {
//        return userService
//                .login(username, password)
//                .orElseThrow(() -> new RuntimeException("invalid login and/or password"));
//    }
}
