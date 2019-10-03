package com.olrox.chat.rest;

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

    @PostMapping("/register/agent/{name}")
    public UserDetailsDto registerAgent(@PathVariable String name) {
        User user = userService.addUnauthorizedUser(ConnectionType.OFFLINE);
        user = userService.register(user, name, Role.Type.AGENT);

        return converter.toDetailsDto(user);
    }

    @PostMapping("/register/client/{name}")
    public UserDetailsDto registerClient(@PathVariable String name) {
        User user = userService.addUnauthorizedUser(ConnectionType.OFFLINE);
        user = userService.register(user, name, Role.Type.CLIENT);

        return converter.toDetailsDto(user);
    }
}
