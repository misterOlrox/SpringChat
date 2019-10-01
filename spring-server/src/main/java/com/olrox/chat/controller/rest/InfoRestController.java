package com.olrox.chat.controller.rest;

import com.olrox.chat.entity.User;
import com.olrox.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/info")
public class InfoRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/agents")
    List<User> allAgents() {
        return userService.getAllAgents();
    }

    @GetMapping("/agents/free")
    List<User> freeAgents() {
        return userService.getFreeAgents();
    }
}
