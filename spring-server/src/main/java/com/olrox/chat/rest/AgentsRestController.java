package com.olrox.chat.rest;

import com.olrox.chat.dto.AmountDto;
import com.olrox.chat.dto.DetailedUserDto;
import com.olrox.chat.dto.UserDto;
import com.olrox.chat.entity.User;
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
@RequestMapping("/api/info/agents")
public class AgentsRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    HttpEntity<Page<UserDto>> allAgents(@RequestParam(defaultValue = "0") Integer pageNumber,
                                        @RequestParam(defaultValue = "10") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userService.getAllAgentsPage(pageable);
        Page<UserDto> dtoPage = page.map(UserDto::new);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    @GetMapping("/free")
    HttpEntity<Page<UserDto>> freeAgents(@RequestParam(defaultValue = "0") Integer pageNumber,
                             @RequestParam(defaultValue = "10") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userService.getFreeAgents(pageable);
        Page<UserDto> dtoPage = page.map(UserDto::new);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    HttpEntity<DetailedUserDto> agentInfo(@PathVariable Long id) {
        User user = userService.getUserById(id);
        DetailedUserDto userDto = new DetailedUserDto(user);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/free/amount")
    HttpEntity<AmountDto> agentsAmount() {
        Long amount = userService.countFreeAgents();
        AmountDto amountDto = new AmountDto(amount, "Quantity of free agents");

        return new ResponseEntity<>(amountDto, HttpStatus.OK);
    }
}
