package com.olrox.chat.rest;

import com.olrox.chat.dto.DetailedUserDto;
import com.olrox.chat.dto.MessageDto;
import com.olrox.chat.dto.UserDto;
import com.olrox.chat.entity.*;
import com.olrox.chat.service.MessageService;
import com.olrox.chat.service.SupportChatRoomService;
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

    @Autowired
    private SupportChatRoomService supportChatRoomService;

    @PostMapping("/user")
    public HttpEntity<DetailedUserDto> register(@RequestBody UserDto userDto) {
        User user = userService.addUnauthorizedUser(ConnectionType.OFFLINE);
        user = userService.register(user, userDto.getName(), userDto.getRole());

        DetailedUserDto responseUserDto = new DetailedUserDto(user);

        return new ResponseEntity<>(responseUserDto, HttpStatus.CREATED);
    }

    @GetMapping("/messages/{userId}")
    public HttpEntity<Page<MessageDto>> getUnreadMessages(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                                          @PathVariable Long userId) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        User user = userService.getUserById(userId);
        Page<Message> page = messageService.getUnreceivedMessages(user, pageable);
        Page<MessageDto> dtoPage = page.map(MessageDto::new);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    @PostMapping("/message")
    public HttpEntity<MessageDto> sendMessage(@RequestBody MessageDto messageDto) {
        User sender = userService.getUserById(messageDto.getSenderId());
        Message message = messageService.createUserMessage(sender, messageDto.getText(), MessageType.USER_TO_CHAT);
        Message result = supportChatRoomService.broadcast(message);

        MessageDto resultDto = new MessageDto(result);

        return new ResponseEntity<>(resultDto, HttpStatus.CREATED);
    }

    @PostMapping("/message/leaving/{userId}")
    public HttpEntity<MessageDto> leave(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        Message leaveMessage = messageService.createUserMessage(user, "Leaving message from REST", MessageType.USER_TO_SERVER);
        Message leaveResponse = supportChatRoomService.leaveChat(user, leaveMessage);

        MessageDto responseDto = new MessageDto(leaveResponse);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public HttpEntity<DetailedUserDto> exit(@PathVariable Long id) {
        User user = userService.getUserById(id);
        userService.handleExit(user);

        DetailedUserDto responseDto = new DetailedUserDto(user);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
