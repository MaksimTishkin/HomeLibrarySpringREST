package com.epam.tishkin.server.service.impl;

import com.epam.tishkin.models.User;
import com.epam.tishkin.server.repository.UserRepository;
import com.epam.tishkin.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User authorization(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }

    @Override
    public User addNewUser(User libraryUser) {
        return userRepository.save(libraryUser);
    }

    @Override
    public boolean deleteUser(String login) {
        return userRepository.deleteByLogin(login);
    }

    public String getRoleByLogin(String login) {
        return userRepository.findUserRoleByLogin(login);
    }

    /*
    @Override
    public List<String> showHistory() {
        return HistoryManager.read();
    }
    */
}
