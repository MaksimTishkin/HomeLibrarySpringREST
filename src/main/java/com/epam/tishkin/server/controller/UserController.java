package com.epam.tishkin.server.controller;

import com.epam.tishkin.messages.SignInForm;
import com.epam.tishkin.models.User;
import com.epam.tishkin.server.security.jwt.JwtProvider;
import com.epam.tishkin.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JwtProvider jwtProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping(value = "/auth/signin")
    public ResponseEntity<String> authenticateUser(
            @RequestBody SignInForm signInForm) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signInForm.getLogin(), signInForm.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(jwt);
    }

    @PostMapping(value = "/get-role/{login}")
    public ResponseEntity<String> getRoleByLogin(@PathVariable(name = "login") String login) {
        String role = userService.getRoleByLogin(login);
        return ResponseEntity.ok(role);
    }

    @PostMapping(value = "/add")
    public User addUser(@RequestBody User user) {
        return userService.addNewUser(user);
    }

    @DeleteMapping(value = "/delete/{login}")
    public void deleteUser(@PathVariable(name = "login") String login) {
        userService.deleteUser(login);
    }
}
