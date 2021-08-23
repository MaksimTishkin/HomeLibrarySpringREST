package com.epam.tishkin.server.service;

import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<Void> authenticate(String login, String password);
    String getRoleByLogin(String login);
}
