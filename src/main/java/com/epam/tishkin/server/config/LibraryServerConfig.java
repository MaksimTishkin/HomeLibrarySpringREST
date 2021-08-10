package com.epam.tishkin.server.config;

import com.epam.tishkin.models.Book;
import com.epam.tishkin.server.dao.LibraryDAO;
import com.epam.tishkin.server.dao.impl.LibraryDAOImpl;
import com.epam.tishkin.server.repository.BookRepository;
import com.epam.tishkin.server.service.BookService;
import com.epam.tishkin.server.service.impl.BookServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.epam.tishkin.server.repository")
public class LibraryServerConfig {

    @Bean
    public LibraryDAO libraryDAO() {
        return new LibraryDAOImpl();
    }

    @Bean
    @DependsOn("libraryDAO")
    public BookService bookService(LibraryDAO libraryDAO, BookRepository bookRepository) {
        return new BookServiceImpl(libraryDAO, bookRepository);
    }
}
