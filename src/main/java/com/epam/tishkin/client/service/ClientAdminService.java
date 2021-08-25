package com.epam.tishkin.client.service;

import com.epam.tishkin.client.util.JwtHeadersUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ClientAdminService {
    private static final String REST_URI = "http://localhost:8088";
    private final RestTemplate restTemplate;

    @Autowired
    public ClientAdminService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String registerNewUser(String login, String password) {
        try {
            HttpHeaders headers = JwtHeadersUtil.getHeadersCookieWithJwt();
            headers.add("login", login);
            headers.add("password", password);
            ResponseEntity<String> response = restTemplate.postForEntity(REST_URI + "/admin/register",
                    new HttpEntity<String>(headers), String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.FORBIDDEN) || e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
                return "Access denied";
            }
            return "Ops.... something is wrong " + e.getStatusCode();
        }
    }

    public String blockUser(String login) {
        HttpHeaders headers = JwtHeadersUtil.getHeadersCookieWithJwt();
        ResponseEntity<String> response = restTemplate.exchange(REST_URI + "admin/delete/{login}",
                HttpMethod.DELETE, new HttpEntity<String>(headers), String.class, login);
        return response.getBody();
    }

    //TODO: implement writing and reading history
    public List<String> showHistory() {
        ResponseEntity<List<String>> history = restTemplate
                .exchange(REST_URI + "users/show-history", HttpMethod.GET,
                        null, new ParameterizedTypeReference<>(){});
        return history.getBody();
    }
}
