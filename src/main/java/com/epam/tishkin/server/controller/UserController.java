package com.epam.tishkin.server.controller;

import com.epam.tishkin.server.repository.UserRepository;
import com.epam.tishkin.server.security.jwt.JwtProvider;
import com.epam.tishkin.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @PostMapping(value = "/get-role/{login}")
    public ResponseEntity<String> getRoleByLogin(@PathVariable(name = "login") String login) {
        String role = userService.getRoleByLogin(login);
        return ResponseEntity.ok(role);
    }
}
