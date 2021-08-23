package com.epam.tishkin.server.service;

import com.epam.tishkin.models.User;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    ResponseEntity<String> registerUser(User user);
    ResponseEntity<String> deleteUser(String login);
    //List<String> showHistory();
}
