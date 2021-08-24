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
        if (userRepository.findById(user.getLogin()).isPresent()) {
            throw new EntityExistsException("Username is already taken: " + user.getLogin());
        }
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully: " + user.getLogin());
    }

    public ResponseEntity<String> deleteUser(String login) {
        userRepository.findById(login)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + login));
        userRepository.deleteById(login);
        return ResponseEntity.ok("User deleted: " + login);
    }
}
