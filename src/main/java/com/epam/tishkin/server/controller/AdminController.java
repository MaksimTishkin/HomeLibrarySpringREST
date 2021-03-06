package com.epam.tishkin.server.controller;

import com.epam.tishkin.model.Role;
import com.epam.tishkin.model.User;
import com.epam.tishkin.server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping(value = "/register")
    public ResponseEntity<String> registerUser(
            @RequestHeader("login") @NotEmpty(message = "Please provide a login") String login,
            @RequestHeader("password") @NotEmpty(message = "Please provide a password") String password) {
        return ResponseEntity.ok(adminService.addUser(login, password));
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
