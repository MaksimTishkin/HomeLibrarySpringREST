package com.epam.tishkin.client.config;

import com.epam.tishkin.client.handler.RestTemplateResponseErrorHandler;
import com.epam.tishkin.client.service.*;
import com.epam.tishkin.client.util.JwtHeadersUtil;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource("application.properties")
public class LibraryClientConfig {

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean
    @DependsOn("restTemplateBuilder")
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.errorHandler(responseErrorHandler()).build();
    }

    @Bean
    public RestTemplateResponseErrorHandler responseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    @DependsOn({"restTemplate", "jwtHeadersUtil"})
    public ClientBookService clientBookService(RestTemplate restTemplate, JwtHeadersUtil jwtHeadersUtil) {
        return new ClientBookService(restTemplate, jwtHeadersUtil);
    }

    @Bean
    @DependsOn({"restTemplate", "jwtHeadersUtil"})
    public ClientAuthorService clientAuthorService(RestTemplate restTemplate, JwtHeadersUtil jwtHeadersUtil) {
        return new ClientAuthorService(restTemplate, jwtHeadersUtil);
    }

    @Bean
    @DependsOn({"restTemplate", "jwtHeadersUtil"})
    public ClientUserService clientUserService(RestTemplate restTemplate, JwtHeadersUtil jwtHeadersUtil) {
        return new ClientUserService(restTemplate, jwtHeadersUtil);
    }

    @Bean
    @DependsOn({"restTemplate", "jwtHeadersUtil"})
    public ClientBookmarkService clientBookmarkService(RestTemplate restTemplate, JwtHeadersUtil jwtHeadersUtil) {
        return new ClientBookmarkService(restTemplate, jwtHeadersUtil);
    }

    @Bean
    @DependsOn({"restTemplate", "jwtHeadersUtil"})
    public ClientAdminService clientAdminService(RestTemplate restTemplate, JwtHeadersUtil jwtHeadersUtil) {
        return new ClientAdminService(restTemplate, jwtHeadersUtil);
    }

    @Bean
    public JwtHeadersUtil jwtHeadersUtil() {
        return new JwtHeadersUtil();
    }
}
