package com.epam.tishkin.client.config;

import com.epam.tishkin.client.handler.RestTemplateResponseErrorHandler;
import com.epam.tishkin.client.service.*;
import com.epam.tishkin.client.manager.JwtHeadersManager;
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
    public BookService clientBookService(RestTemplate restTemplate, JwtHeadersManager jwtHeadersManager) {
        return new BookService(restTemplate, jwtHeadersManager);
    }

    @Bean
    @DependsOn({"restTemplate", "jwtHeadersUtil"})
    public AuthorService clientAuthorService(RestTemplate restTemplate, JwtHeadersManager jwtHeadersManager) {
        return new AuthorService(restTemplate, jwtHeadersManager);
    }

    @Bean
    @DependsOn({"restTemplate", "jwtHeadersUtil"})
    public UserService clientUserService(RestTemplate restTemplate, JwtHeadersManager jwtHeadersManager) {
        return new UserService(restTemplate, jwtHeadersManager);
    }

    @Bean
    @DependsOn({"restTemplate", "jwtHeadersUtil"})
    public BookmarkService clientBookmarkService(RestTemplate restTemplate, JwtHeadersManager jwtHeadersManager) {
        return new BookmarkService(restTemplate, jwtHeadersManager);
    }

    @Bean
    @DependsOn({"restTemplate", "jwtHeadersUtil"})
    public AdminService clientAdminService(RestTemplate restTemplate, JwtHeadersManager jwtHeadersManager) {
        return new AdminService(restTemplate, jwtHeadersManager);
    }

    @Bean
    public JwtHeadersManager jwtHeadersUtil() {
        return new JwtHeadersManager();
    }
}
