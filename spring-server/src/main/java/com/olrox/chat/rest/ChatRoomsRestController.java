package com.olrox.chat.rest;

import com.olrox.chat.dto.DetailedSupportChatRoomDto;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/info/chats")
public class ChatRoomsRestController {

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

    @GetMapping("/{id}")
    public HttpEntity<DetailedSupportChatRoomDto> chatRoomDetails(@PathVariable Long id) {
        SupportChatRoom supportChatRoom = supportChatRoomService.getRoomById(id);
        DetailedSupportChatRoomDto dto = new DetailedSupportChatRoomDto(supportChatRoom);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }


}
