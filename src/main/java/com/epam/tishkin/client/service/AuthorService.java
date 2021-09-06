package com.epam.tishkin.client.service;

import com.epam.tishkin.client.exception.CustomResponseException;
import com.epam.tishkin.client.manager.JwtHeadersManager;
import com.epam.tishkin.model.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AuthorService {
    private static final String REST_URL = "http://localhost:8088/authors";
    private static final String ADD_AUTHOR_URI = REST_URL + "/add";
    private static final String DELETE_AUTHOR_URI = REST_URL + "/delete";
    private final RestTemplate restTemplate;
    private final JwtHeadersManager jwtHeaders;

    @Autowired
    public AuthorService(RestTemplate restTemplate, JwtHeadersManager jwtHeaders) {
        this.restTemplate = restTemplate;
        this.jwtHeaders = jwtHeaders;
    }

    public String addAuthor(String authorName) {
        Author author = new Author(authorName);
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(ADD_AUTHOR_URI,
                    new HttpEntity<>(author, headers), String.class);
            return response.getBody();
        } catch (CustomResponseException e) {
            return e.getMessage();
        }
    }

    public String deleteAuthor(String authorName) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        try {
            ResponseEntity<String> response = restTemplate.exchange(DELETE_AUTHOR_URI + "/{authorName}",
                    HttpMethod.DELETE, new HttpEntity<String>(headers), String.class, authorName);
            return response.getBody();
        } catch (CustomResponseException e) {
            return e.getMessage();
        }
    }
}
