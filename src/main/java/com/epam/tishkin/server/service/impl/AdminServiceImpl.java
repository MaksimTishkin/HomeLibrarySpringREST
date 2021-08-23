package com.epam.tishkin.server.service.impl;

import com.epam.tishkin.models.User;
import com.epam.tishkin.server.repository.UserRepository;
import com.epam.tishkin.server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> registerUser(User user) {
        userRepository.findById(user.getLogin())
                .orElseThrow(() -> new EntityExistsException("Username is already taken"));
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    public ResponseEntity<String> deleteUser(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("Not found: " + login));
        userRepository.delete(user);
        return ResponseEntity.ok("User deleted: " + login);
    }
}
