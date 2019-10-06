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
@RequestMapping("/api/info/clients")
public class ClientsRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/queue")
    HttpEntity<Page<UserDto>> clientQueue(@RequestParam(defaultValue = "0") Integer pageNumber,
                                          @RequestParam(defaultValue = "10") Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = userService.getClientQueue(pageable);
        Page<UserDto> dtoPage = page.map(UserDto::new);

        return new ResponseEntity<>(dtoPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    HttpEntity<DetailedUserDto> clientInfo(@PathVariable Long id) {
        User user = userService.getClient(id);
        DetailedUserDto userDto = new DetailedUserDto(user);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
}
