package com.olrox.chat.rest;

import com.olrox.chat.dto.DetailedUserDto;
import com.olrox.chat.dto.MessageDto;
import com.olrox.chat.dto.UserDto;
import com.olrox.chat.entity.*;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

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

    @GetMapping("/messages/{userId}")
    public HttpEntity<Page<MessageDto>> getUnreadMessages(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          @PathVariable(required = true) Long userId) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Message> page = messageService.getUnreceivedMessages(userId, pageable);
        Page<MessageDto> dtoPage = page.map((x) -> new MessageDto(x, MessageDetail.Status.NOT_RECEIVED));

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

//    @PostMapping("/send")
//    public HttpEntity<MessageDto> send() {
//
//    }
}
