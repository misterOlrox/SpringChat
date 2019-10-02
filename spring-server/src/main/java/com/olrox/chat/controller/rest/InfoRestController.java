package com.olrox.chat.controller.rest;

import com.olrox.chat.entity.User;
import com.olrox.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/info")
public class InfoRestController {

    @Autowired
    private UserService userService;

//    @GetMapping("/agents")
//    List<User> allAgents() {
//        return userService.getAllAgents();
//    }

    @GetMapping("/agents")
    Page<User> allAgents(@PageableDefault(page = 0, size = 20) Pageable pageable) {
        return userService.getAllAgentsPage(pageable);
    }

    @GetMapping("/agents/free")
    List<User> freeAgents() {
//        BeanUtils.copyProperties();
//        PageRequest.of()
        return userService.getFreeAgents();
    }
}
