package com.epam.tishkin.server.controller;

import com.epam.tishkin.models.Role;
import com.epam.tishkin.models.User;
import com.epam.tishkin.server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final PasswordEncoder encoder;

    @Autowired
    public AdminController(AdminService adminService, PasswordEncoder encoder) {
        this.adminService = adminService;
        this.encoder = encoder;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(
            @RequestHeader("login") String login,
            @RequestHeader("password") String password) {
        User user = new User(login, encoder.encode(password), Role.VISITOR);
        return adminService.registerUser(user);
    }

    @DeleteMapping(value = "/delete/{login}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "login") String login) {
        return adminService.deleteUser(login);
    }
}
