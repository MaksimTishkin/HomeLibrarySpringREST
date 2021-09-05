package com.epam.tishkin.client.service;

import com.epam.tishkin.client.exception.CustomResponseException;
import com.epam.tishkin.client.manager.JwtHeadersManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class AdminService {
    @Value("${rest.uri}")
    private String REST_URI;
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
            ResponseEntity<String> response = restTemplate.postForEntity(REST_URI + "/admin/register",
                    new HttpEntity<String>(headers), String.class);
            return response.getBody();
        } catch (CustomResponseException e) {
            return e.getMessage();
        }
    }

    public String blockUser(String login) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        try {
            ResponseEntity<String> response = restTemplate.exchange(REST_URI + "/admin/delete/{login}",
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
                    .exchange(REST_URI + "/admin/get-history", HttpMethod.GET,
                            new HttpEntity<String>(headers), new ParameterizedTypeReference<>(){});
            history = response.getBody();
        } catch (CustomResponseException e) {
            logger.error(e.getMessage());
        }
        return history;
    }
}
