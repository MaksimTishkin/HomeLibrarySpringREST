package com.epam.tishkin.server.service.impl;

import com.epam.tishkin.model.Role;
import com.epam.tishkin.model.User;
import com.epam.tishkin.server.repository.UserRepository;
import com.epam.tishkin.server.service.AdminService;
import com.epam.tishkin.server.manager.HistoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;

public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final HistoryManager historyManager;
    private final PasswordEncoder encoder;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository,
                            HistoryManager historyManager,
                            PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.historyManager = historyManager;
        this.encoder = encoder;
    }

    public String addUser(String login, String password) {
        if (userRepository.findById(login).isPresent()) {
            throw new EntityExistsException("Username is already taken: " + login);
        }
        User user = new User(login, encoder.encode(password), Role.ROLE_VISITOR);
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
