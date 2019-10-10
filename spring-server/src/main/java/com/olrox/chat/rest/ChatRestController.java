package com.olrox.chat.rest;

import com.olrox.chat.dto.DetailedUserDto;
import com.olrox.chat.dto.MessageDto;
import com.olrox.chat.entity.Message;
import com.olrox.chat.entity.MessageType;
import com.olrox.chat.entity.User;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SupportChatRoomService supportChatRoomService;

    @GetMapping("/messages")
    public HttpEntity<Page<MessageDto>> getUnreadMessages(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0", required = false) Integer pageNumber,
            @RequestParam(defaultValue = "10", required = false) Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Message> page = messageService.getUnreceivedMessages(user, pageable);
        Page<MessageDto> dtoPage = page.map(MessageDto::new);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    @PostMapping("/message")
    public HttpEntity<MessageDto> sendMessage(
            @AuthenticationPrincipal User user,
            @RequestBody MessageDto messageDto) {

        Message message = messageService.createUserMessage(user, messageDto.getText(), MessageType.USER_TO_CHAT);
        Message result = supportChatRoomService.broadcast(message);

        MessageDto resultDto = new MessageDto(result);

        return new ResponseEntity<>(resultDto, HttpStatus.CREATED);
    }

    @PostMapping("/message/leaving")
    public HttpEntity<MessageDto> leave(@AuthenticationPrincipal User user) {
        Message leaveMessage = messageService.createUserMessage(user,
                "Leaving message from REST",
                MessageType.USER_TO_SERVER);

        Message leaveResponse = supportChatRoomService.leaveChat(user, leaveMessage);

        MessageDto responseDto = new MessageDto(leaveResponse);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/exit")
    public HttpEntity<DetailedUserDto> exit(@AuthenticationPrincipal User user) {
        userService.handleExit(user);

        DetailedUserDto responseDto = new DetailedUserDto(user);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
