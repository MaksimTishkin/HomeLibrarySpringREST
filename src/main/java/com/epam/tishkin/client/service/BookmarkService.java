package com.epam.tishkin.client.service;

import com.epam.tishkin.client.exception.CustomResponseException;
import com.epam.tishkin.client.manager.JwtHeadersManager;
import com.epam.tishkin.model.Bookmark;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class BookmarkService {
    private static final String REST_URL = "http://localhost:8088/bookmarks";
    private static final String ADD_BOOKMARK_URI = REST_URL + "/add";
    private static final String DELETE_BOOKMARK_URI = REST_URL + "/delete";
    private static final String GET_BOOKMARKS_URI = REST_URL + "/get";
    private final RestTemplate restTemplate;
    private final JwtHeadersManager jwtHeaders;
    Logger logger = LogManager.getLogger(BookmarkService.class);

    @Autowired
    public BookmarkService(RestTemplate restTemplate, JwtHeadersManager jwtHeaders) {
        this.restTemplate = restTemplate;
        this.jwtHeaders = jwtHeaders;
    }

    public String addBookmark(String title, int page) {
        Bookmark bookmark = new Bookmark(title, page);
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        try {
            HttpEntity<String> response = restTemplate.postForEntity(ADD_BOOKMARK_URI,
                    new HttpEntity<>(bookmark, headers), String.class);
            return response.getBody();
        } catch (CustomResponseException e) {
            return e.getMessage();
        }
    }

    public String deleteBookmark(String title) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        try {
            HttpEntity<String> response = restTemplate.exchange(DELETE_BOOKMARK_URI + "/{title}",
                    HttpMethod.DELETE, new HttpEntity<>(headers), String.class, title);
            return response.getBody();
        } catch (CustomResponseException e) {
            return e.getMessage();
        }
    }

    public List<Bookmark> showBookmarks() {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        List<Bookmark> bookmarks = null;
        try {
            ResponseEntity<List<Bookmark>> response = restTemplate
                    .exchange(GET_BOOKMARKS_URI, HttpMethod.GET,
                            new HttpEntity<>(headers), new ParameterizedTypeReference<>(){});
            bookmarks = response.getBody();
        } catch (CustomResponseException e) {
            logger.error(e.getMessage());
        }
        return bookmarks;
    }
}
