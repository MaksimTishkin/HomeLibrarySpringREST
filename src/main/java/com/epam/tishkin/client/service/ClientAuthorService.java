package com.epam.tishkin.client.service;

import com.epam.tishkin.models.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

public class ClientAuthorService {
    private static final String REST_URI = "http://localhost:8083";
    private final RestTemplate restTemplate;

    @Autowired
    public ClientAuthorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean addAuthor(String authorName) {
        Author author = restTemplate.postForObject(REST_URI + "/authors/add", new Author(authorName), Author.class);
        return author != null;
    }

    public void deleteAuthor(String authorName) {
        restTemplate.delete(REST_URI + "/authors/delete/{authorName}", authorName);
    }
}
