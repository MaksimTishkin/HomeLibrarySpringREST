package com.epam.tishkin.client.service;

import com.epam.tishkin.models.Bookmark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ClientBookmarkService {
    private static final String REST_URI = "http://localhost:8088";
    private final RestTemplate restTemplate;

    @Autowired
    public ClientBookmarkService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean addBookmark(String bookTitle, int page) {
        Bookmark bookmark = restTemplate.postForObject(REST_URI + "/bookmarks/add", new Bookmark(bookTitle, page), Bookmark.class);
        return bookmark != null;
    }

    public void deleteBookmark(String bookTitle) {
        restTemplate.delete(REST_URI + "/bookmarks/delete/{title}", bookTitle);
    }

    public List<Bookmark> showBookmarks() {
        ResponseEntity<List<Bookmark>> foundBookmarks = restTemplate
                .exchange(REST_URI + "/bookmarks/get-bookmarks", HttpMethod.GET,
                        null, new ParameterizedTypeReference<>(){});
        return foundBookmarks.getBody();
    }
}
