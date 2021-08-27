package com.epam.tishkin.client.service;

import com.epam.tishkin.client.utils.JwtHeadersUtil;
import com.epam.tishkin.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class ClientUserService {
    private static final String REST_URI = "http://localhost:8088";
    private final RestTemplate restTemplate;

    @Autowired
    public ClientUserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Role authenticate(String login, String password) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("login", login);
        httpHeaders.add("password", password);
        try {
            ResponseEntity<Void> response = restTemplate.exchange(REST_URI + "/users/authenticate",
                    HttpMethod.POST, new HttpEntity<String>(httpHeaders), Void.class);
            String jwt = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
            JwtHeadersUtil.setJwt(jwt);
            return getRole();
        } catch (HttpClientErrorException.Unauthorized e) {
            return null;
        }
    }

    public Role getRole() {
        HttpHeaders headers = JwtHeadersUtil.getHeadersCookieWithJwt();
        ResponseEntity<Role> response = restTemplate
        .exchange(REST_URI + "/users/get-role",
                HttpMethod.POST, new HttpEntity<>(headers), Role.class);
        return response.getBody();
    }
}
