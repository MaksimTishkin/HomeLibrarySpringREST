package com.epam.tishkin.client.config;

import com.epam.tishkin.client.service.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.client.RestTemplate;

@Configuration
public class LibraryClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @DependsOn("restTemplate")
    public ClientBookService clientBookService(RestTemplate restTemplate) {
        return new ClientBookService(restTemplate);
    }

    @Bean
    @DependsOn("restTemplate")
    public ClientAuthorService clientAuthorService(RestTemplate restTemplate) {
        return new ClientAuthorService(restTemplate);
    }

    @Bean
    @DependsOn("restTemplate")
    public ClientUserService clientUserService(RestTemplate restTemplate) {
        return new ClientUserService(restTemplate);
    }

    @Bean
    @DependsOn("restTemplate")
    public ClientBookmarkService clientBookmarkService(RestTemplate restTemplate) {
        return new ClientBookmarkService(restTemplate);
    }

    @Bean
    @DependsOn("restTemplate")
    public ClientAdminService clientAdminService(RestTemplate restTemplate) {
        return new ClientAdminService(restTemplate);
    }
}
