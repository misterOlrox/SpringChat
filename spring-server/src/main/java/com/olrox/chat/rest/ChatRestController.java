package com.olrox.chat.rest;

import com.olrox.chat.dto.DetailedUserDto;
import com.olrox.chat.dto.UserDto;
import com.olrox.chat.entity.ConnectionType;
import com.olrox.chat.entity.Role;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/register/agent/{name}")
    public HttpEntity<DetailedUserDto> registerAgent(@PathVariable String name) {
        User user = userService.addUnauthorizedUser(ConnectionType.OFFLINE);
        user = userService.register(user, name, Role.Type.AGENT);

        DetailedUserDto userDto = new DetailedUserDto(user);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PostMapping("/register/client/{name}")
    public HttpEntity<DetailedUserDto> registerClient(@PathVariable String name) {
        User user = userService.addUnauthorizedUser(ConnectionType.OFFLINE);
        user = userService.register(user, name, Role.Type.CLIENT);

        DetailedUserDto userDto = new DetailedUserDto(user);

        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

//    @PostMapping("/send")
//    public HttpEntity<MessageDto> send() {
//
//    }
}
