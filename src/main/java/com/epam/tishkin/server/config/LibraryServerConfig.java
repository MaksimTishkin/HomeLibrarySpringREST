package com.epam.tishkin.server.config;

import com.epam.tishkin.server.dao.LibraryDAO;
import com.epam.tishkin.server.dao.impl.LibraryDAOImpl;
import com.epam.tishkin.server.service.BookService;
import com.epam.tishkin.server.service.impl.BookServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class LibraryServerConfig {

    @Bean
    public LibraryDAO libraryDAO() {
        return new LibraryDAOImpl();
    }

    @Bean
    @DependsOn("libraryDAO")
    public BookService bookService(LibraryDAO libraryDAO) {
        return new BookServiceImpl(libraryDAO);
    }
}
