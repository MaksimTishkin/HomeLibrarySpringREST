package com.epam.tishkin.server.service.impl;

import com.epam.tishkin.models.Role;
import com.epam.tishkin.models.User;
import com.epam.tishkin.server.repository.UserRepository;
import com.epam.tishkin.server.security.jwt.JwtProvider;
import com.epam.tishkin.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           AuthenticationManager authenticationManager,
                           JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    public ResponseEntity<Void> authenticate(String login, String password) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwt).build();
    }

    public Role getRoleByLogin() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByLogin(currentUsername);
        return user.getRole();
    }
}
