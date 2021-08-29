package com.epam.tishkin.client.service;

import com.epam.tishkin.client.exception.CustomResponseException;
import com.epam.tishkin.client.util.JwtHeadersUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ClientAuthorService {
    @Value("${rest.uri}")
    private String REST_URI;
    private final RestTemplate restTemplate;
    private final JwtHeadersUtil jwtHeaders;

    @Autowired
    public ClientAuthorService(RestTemplate restTemplate, JwtHeadersUtil jwtHeaders) {
        this.restTemplate = restTemplate;
        this.jwtHeaders = jwtHeaders;
    }

    public String addAuthor(String authorName) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(REST_URI + "/authors/add/{authorName}",
                    new HttpEntity<String>(headers), String.class, authorName);
            return response.getBody();
        } catch (CustomResponseException e) {
            return e.getMessage();
        }
    }

    public String deleteAuthor(String authorName) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        try {
            ResponseEntity<String> response = restTemplate.exchange(REST_URI + "/authors/delete/{authorName}",
                    HttpMethod.DELETE, new HttpEntity<String>(headers), String.class, authorName);
            return response.getBody();
        } catch (CustomResponseException e) {
            return e.getMessage();
        }
    }
}
