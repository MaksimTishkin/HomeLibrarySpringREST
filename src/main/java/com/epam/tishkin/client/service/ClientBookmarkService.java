package com.epam.tishkin.client.service;

import com.epam.tishkin.client.util.JwtHeadersUtil;
import com.epam.tishkin.models.Bookmark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

    public String addBookmark(String title, int page) {
        HttpHeaders headers = JwtHeadersUtil.getHeadersCookieWithJwt();
        HttpEntity<String> response = restTemplate.postForEntity(REST_URI + "/bookmarks/add/{title}/{page}",
                new HttpEntity<>(headers), String.class, title, page);
        return response.getBody();
    }

    public String deleteBookmark(String title) {
        HttpHeaders headers = JwtHeadersUtil.getHeadersCookieWithJwt();
        HttpEntity<String> response = restTemplate.exchange(REST_URI + "/bookmarks/delete/{title}",
                HttpMethod.DELETE, new HttpEntity<>(headers), String.class, title);
        return response.getBody();
    }

    public List<Bookmark> showBookmarks() {
        HttpHeaders headers = JwtHeadersUtil.getHeadersCookieWithJwt();
        ResponseEntity<List<Bookmark>> response = restTemplate
                .exchange(REST_URI + "/bookmarks/get", HttpMethod.GET,
                        new HttpEntity<>(headers), new ParameterizedTypeReference<>(){});
        return response.getBody();
    }
}
