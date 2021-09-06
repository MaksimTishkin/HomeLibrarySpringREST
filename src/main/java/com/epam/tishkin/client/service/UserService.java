package com.epam.tishkin.client.service;

import com.epam.tishkin.client.exception.CustomResponseException;
import com.epam.tishkin.client.manager.JwtHeadersManager;
import com.epam.tishkin.model.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class UserService {
    private static final String REST_URL = "http://localhost:8088/users";
    private final static String AUTHENTICATE_URI = REST_URL + "/authenticate";
    private final static String GET_ROLE_URI = REST_URL + "/get-role";
    private final RestTemplate restTemplate;
    private final JwtHeadersManager jwtHeaders;
    Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    public UserService(RestTemplate restTemplate, JwtHeadersManager jwtHeaders) {
        this.restTemplate = restTemplate;
        this.jwtHeaders = jwtHeaders;
    }

    public Role authenticate(String login, String password) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("login", login);
        httpHeaders.add("password", password);
        try {
            ResponseEntity<Void> response = restTemplate.exchange(AUTHENTICATE_URI,
                    HttpMethod.POST, new HttpEntity<String>(httpHeaders), Void.class);
            String jwt = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            jwtHeaders.setJwt(jwt);
            return getRole();
        } catch (CustomResponseException e) {
            logger.info("Incorrect login/password");
            return null;
        }
    }

    private Role getRole() {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        ResponseEntity<Role> response = restTemplate
        .exchange(GET_ROLE_URI, HttpMethod.POST, new HttpEntity<>(headers), Role.class);
        return response.getBody();
    }
}
