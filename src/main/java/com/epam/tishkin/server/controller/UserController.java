package com.epam.tishkin.server.controller;

import com.epam.tishkin.models.User;
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

    //TODO: the method should return jwt token
    @PostMapping(value = "/authenticate")
    public User authenticate(
            @RequestHeader(name = "login") String login,
            @RequestHeader(name = "password") String password) {
        return userService.authorization(login, password);
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
