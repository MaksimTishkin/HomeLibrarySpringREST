package com.epam.tishkin.client.config;

import com.epam.tishkin.client.service.ClientServiceREST;
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
    public ClientServiceREST clientServiceREST(RestTemplate restTemplate) {
        return new ClientServiceREST(restTemplate);
    }
}
