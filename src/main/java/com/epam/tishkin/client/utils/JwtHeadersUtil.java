package com.epam.tishkin.client.utils;

import org.springframework.http.HttpHeaders;

public class JwtHeadersUtil {
    private static String jwt;

    public static HttpHeaders getHeadersCookieWithJwt() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, jwt);
        return headers;
    }

    public static void setJwt(String jwt) {
        JwtHeadersUtil.jwt = jwt;
    }
}
