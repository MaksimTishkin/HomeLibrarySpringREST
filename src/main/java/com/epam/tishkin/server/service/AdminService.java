package com.epam.tishkin.server.service;

import com.epam.tishkin.model.User;

import java.util.List;

public interface AdminService {

    String addUser(User user);
    String deleteUser(String login);
    List<String> showHistory();
}
