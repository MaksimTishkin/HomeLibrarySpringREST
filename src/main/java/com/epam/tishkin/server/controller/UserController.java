package com.epam.tishkin.server.controller;

import com.epam.tishkin.model.Role;
import com.epam.tishkin.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<Void> authenticateUser(
            @RequestHeader("login") String login,
            @RequestHeader("password") String password) {
        return userService.authenticate(login, password);
    }

    @PostMapping(value = "/get-role")
    public ResponseEntity<Role> getRoleByLogin() {
        Role role = userService.getRoleByLogin();
        return ResponseEntity.ok(role);
    }
}
