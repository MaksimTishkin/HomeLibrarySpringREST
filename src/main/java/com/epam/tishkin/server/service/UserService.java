package com.epam.tishkin.server.service;

import com.epam.tishkin.models.User;

public interface UserService {
    User authorization(String login, String password);
    User addNewUser(User libraryUser);
    boolean deleteUser(String login);
    String getRoleByLogin(String login);
    //List<String> showHistory();
}
