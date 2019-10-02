package com.olrox.chat.controller.rest;

import com.olrox.chat.dto.EntityToDtoConverter;
import com.olrox.chat.dto.UserDetailsDto;
import com.olrox.chat.dto.UserDto;
import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private EntityToDtoConverter converter;

    @PostMapping("/register")
    public UserDetailsDto register(@RequestBody User user) {
        user.setId(0);
        user.setConnectionType(ConnectionType.OFFLINE);
        user.setChatRooms(null);

        user = userService.register(user, user.getCurrentRoleType());

        return converter.toDetailsDto(user);
    }
}
