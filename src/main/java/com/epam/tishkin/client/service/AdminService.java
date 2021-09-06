package com.epam.tishkin.client.service;

import com.epam.tishkin.client.exception.CustomResponseException;
import com.epam.tishkin.client.manager.JwtHeadersManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class AdminService {
    private static final String REST_URL = "http://localhost:8088/admin";
    private static final String REGISTER_USER_URI = REST_URL + "/register";
    private static final String BLOCK_USER_URI = REST_URL + "/delete";
    private static final String SHOW_HISTORY_URI = REST_URL + "/get-history";
    private final RestTemplate restTemplate;
    private final JwtHeadersManager jwtHeaders;
    Logger logger = LogManager.getLogger(AdminService.class);

    @Autowired
    public AdminService(RestTemplate restTemplate, JwtHeadersManager jwtHeaders) {
        this.restTemplate = restTemplate;
        this.jwtHeaders = jwtHeaders;
    }

    public String registerNewUser(String login, String password) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        headers.add("login", login);
        headers.add("password", password);
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(REGISTER_USER_URI,
                    new HttpEntity<String>(headers), String.class);
            return response.getBody();
        } catch (CustomResponseException e) {
            return e.getMessage();
        }
    }

    public String blockUser(String login) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        try {
            ResponseEntity<String> response = restTemplate.exchange(BLOCK_USER_URI + "/{login}",
                    HttpMethod.DELETE, new HttpEntity<String>(headers), String.class, login);
            return response.getBody();
        } catch (CustomResponseException e) {
            return e.getMessage();
        }
    }

    public List<String> showHistory() {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        List<String> history = null;
        try {
            ResponseEntity<List<String>> response = restTemplate
                    .exchange(SHOW_HISTORY_URI, HttpMethod.GET,
                            new HttpEntity<String>(headers), new ParameterizedTypeReference<>(){});
            history = response.getBody();
        } catch (CustomResponseException e) {
            logger.error(e.getMessage());
        }
        return history;
    }
}
