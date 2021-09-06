package com.epam.tishkin.server.service;

import com.epam.tishkin.model.Role;
import org.springframework.http.ResponseEntity;

public interface UserService {
    String authenticate(String login, String password);
    Role getRoleByLogin();
}
