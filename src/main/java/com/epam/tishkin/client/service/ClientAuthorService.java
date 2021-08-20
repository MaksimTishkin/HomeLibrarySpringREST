package com.epam.tishkin.client.service;

import com.epam.tishkin.models.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

public class ClientAuthorService {
    private static final String REST_URI = "http://localhost:8083";
    private final RestTemplate restTemplate;

    @Autowired
    public ClientAuthorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String addAuthor(String authorName) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(REST_URI + "/authors/add", new Author(authorName), String.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return "Ops.... something is wrong " + e.getStatusCode();
        }
    }

    public String deleteAuthor(String authorName) {
        try {
            ResponseEntity<String> response = restTemplate.exchange(REST_URI + "/authors/delete/{authorName}",
                    HttpMethod.DELETE, null, String.class, authorName);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return "Ops.... something is wrong " + e.getStatusCode();
        }
    }
}
