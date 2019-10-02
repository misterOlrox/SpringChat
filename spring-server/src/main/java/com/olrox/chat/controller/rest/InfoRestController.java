package com.olrox.chat.controller.rest;

import com.olrox.chat.dto.UserDto;
import com.olrox.chat.dto.EntityToDtoConverter;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/info")
public class InfoRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private EntityToDtoConverter entityToDtoConverter;

    @GetMapping("/agents")
    Page<UserDto> allAgents(@RequestParam(defaultValue = "0") Integer pageNumber,
                            @RequestParam(defaultValue = "10") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userService.getAllAgentsPage(pageable);

        return entityToDtoConverter.toDto(page);
    }

    @GetMapping("/agents/free")
    Page<UserDto> freeAgents(@RequestParam(defaultValue = "0") Integer pageNumber,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userService.getFreeAgents(pageable);

        return entityToDtoConverter.toDto(page);
    }

}
