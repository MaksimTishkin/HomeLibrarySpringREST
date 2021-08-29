package com.epam.tishkin.client.service;

import com.epam.tishkin.client.exception.CustomResponseException;
import com.epam.tishkin.client.util.JwtHeadersUtil;
import com.epam.tishkin.model.Bookmark;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ClientBookmarkService {
    @Value("${rest.uri}")
    private String REST_URI;
    private final RestTemplate restTemplate;
    private final JwtHeadersUtil jwtHeaders;
    Logger logger = LogManager.getLogger(ClientBookmarkService.class);

    @Autowired
    public ClientBookmarkService(RestTemplate restTemplate, JwtHeadersUtil jwtHeaders) {
        this.restTemplate = restTemplate;
        this.jwtHeaders = jwtHeaders;
    }

    public String addBookmark(String title, int page) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        try {
            HttpEntity<String> response = restTemplate.postForEntity(REST_URI + "/bookmarks/add/{title}/{page}",
                    new HttpEntity<>(headers), String.class, title, page);
            return response.getBody();
        } catch (CustomResponseException e) {
            return e.getMessage();
        }
    }

    public String deleteBookmark(String title) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        try {
            HttpEntity<String> response = restTemplate.exchange(REST_URI + "/bookmarks/delete/{title}",
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
                    .exchange(REST_URI + "/bookmarks/get", HttpMethod.GET,
                            new HttpEntity<>(headers), new ParameterizedTypeReference<>(){});
            bookmarks = response.getBody();
        } catch (CustomResponseException e) {
            logger.error(e.getMessage());
        }
        return bookmarks;
    }
}
