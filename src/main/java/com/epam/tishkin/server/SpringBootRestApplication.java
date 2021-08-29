package com.epam.tishkin.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.epam.tishkin.model")
@EnableJpaRepositories(basePackages = "com.epam.tishkin.server.repository")
public class SpringBootRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRestApplication.class, args);
    }
}
