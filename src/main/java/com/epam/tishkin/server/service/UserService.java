package com.epam.tishkin.server.service;

import com.epam.tishkin.models.Role;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<Void> authenticate(String login, String password);
    Role getRoleByLogin();
}
