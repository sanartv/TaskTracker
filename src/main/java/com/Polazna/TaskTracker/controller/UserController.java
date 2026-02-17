package com.Polazna.TaskTracker.controller;

import com.Polazna.TaskTracker.dto.CreateUserRequest;
import com.Polazna.TaskTracker.entity.User;
import com.Polazna.TaskTracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }
}
