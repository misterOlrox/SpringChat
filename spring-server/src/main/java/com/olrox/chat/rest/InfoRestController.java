package com.olrox.chat.rest;

import com.olrox.chat.dto.EntityToDtoConverter;
import com.olrox.chat.dto.AmountDto;
import com.olrox.chat.dto.UserDetailsDto;
import com.olrox.chat.dto.UserDto;
import com.olrox.chat.entity.User;
import com.olrox.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/agents/{id}")
    UserDetailsDto agentInfo(@PathVariable Long id) {
        User user = userService.getUserById(id);

        return entityToDtoConverter.toDetailsDto(user);
    }

    @GetMapping("/agents/free/amount")
    AmountDto agentsAmount() {
        Long amount = userService.countFreeAgents();
        return new AmountDto(amount, "Quantity of free agents");
    }

}
