package com.epam.tishkin.server.service;

import com.epam.tishkin.models.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {

    String addUser(User user);
    String deleteUser(String login);
    List<String> showHistory();
}
