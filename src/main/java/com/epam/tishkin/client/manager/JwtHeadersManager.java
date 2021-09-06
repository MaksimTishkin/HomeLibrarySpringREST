package com.epam.tishkin.client.manager;

import org.springframework.http.HttpHeaders;

public class JwtHeadersManager {
    private String jwt;

    public HttpHeaders getHeadersCookieWithJwt() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, jwt);
        return headers;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
