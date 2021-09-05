package com.epam.tishkin.server.service.impl;

import com.epam.tishkin.model.User;
import com.epam.tishkin.server.repository.UserRepository;
import com.epam.tishkin.server.service.AdminService;
import com.epam.tishkin.server.manager.HistoryManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final HistoryManager historyManager;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, HistoryManager historyManager) {
        this.userRepository = userRepository;
        this.historyManager = historyManager;
    }

    public String addUser(User user) {
        if (userRepository.findById(user.getLogin()).isPresent()) {
            throw new EntityExistsException("Username is already taken: " + user.getLogin());
        }
        userRepository.save(user);
        return "User registered successfully: " + user.getLogin();
    }

    public String deleteUser(String login) {
        userRepository.findById(login)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + login));
        userRepository.deleteById(login);
        return "User deleted: " + login;
    }

    public List<String> showHistory() {
        return historyManager.read();
    }
}
