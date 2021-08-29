package com.epam.tishkin.server.controller;

import com.epam.tishkin.model.Role;
import com.epam.tishkin.model.User;
import com.epam.tishkin.server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(
            @RequestHeader("login") String login,
            @RequestHeader("password") String password) {
        User user = new User(login, encoder.encode(password), Role.ROLE_VISITOR);
        return ResponseEntity.ok(adminService.addUser(user));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping(value = "/delete/{login}")
    public ResponseEntity<String> deleteUser(@PathVariable(name = "login") String login) {
        return ResponseEntity.ok(adminService.deleteUser(login));
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping(value = "/get-history")
    public ResponseEntity<List<String>> getHistory() {
        return ResponseEntity.ok(adminService.showHistory());
    }
}
