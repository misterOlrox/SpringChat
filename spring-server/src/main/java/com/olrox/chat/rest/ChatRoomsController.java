package com.olrox.chat.rest;

import com.olrox.chat.dto.SupportChatRoomDto;
import com.olrox.chat.entity.SupportChatRoom;
import com.olrox.chat.service.SupportChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info/chats")
public class ChatRoomsController {

    @Autowired
    private SupportChatRoomService supportChatRoomService;

    @GetMapping("")
    public HttpEntity<Page<SupportChatRoomDto>> openChatRooms(@RequestParam(defaultValue = "0") Integer pageNumber,
                                                          @RequestParam(defaultValue = "10") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<SupportChatRoom> page = supportChatRoomService.getOpenChats(pageable);
        Page<SupportChatRoomDto> dtoPage = page.map(SupportChatRoomDto::new);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }


}
