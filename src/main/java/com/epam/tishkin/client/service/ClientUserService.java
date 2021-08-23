package com.epam.tishkin.client.service;

import com.epam.tishkin.client.util.JwtHeadersUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class ClientUserService {
    private static final String REST_URI = "http://localhost:8088";
    private final RestTemplate restTemplate;

    @Autowired
    public ClientUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void authenticate(String login, String password) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("login", login);
        httpHeaders.add("password", password);
        ResponseEntity<Void> response = restTemplate.exchange(REST_URI + "/users/authenticate",
                HttpMethod.POST, new HttpEntity<String>(httpHeaders), Void.class);
        String jwt = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        JwtHeadersUtil.setJwt(jwt);
    }

    public String getRole(String login) {
        ResponseEntity<String> response = restTemplate.getForEntity(REST_URI + "/users/get-role/{login}", String.class, login);
        return response.getBody();
    }
}
