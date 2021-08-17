package com.epam.tishkin.server.config;

import com.epam.tishkin.server.repository.AuthorRepository;
import com.epam.tishkin.server.repository.BookRepository;
import com.epam.tishkin.server.repository.BookmarkRepository;
import com.epam.tishkin.server.repository.UserRepository;
import com.epam.tishkin.server.service.AuthorService;
import com.epam.tishkin.server.service.BookService;
import com.epam.tishkin.server.service.BookmarkService;
import com.epam.tishkin.server.service.UserService;
import com.epam.tishkin.server.service.impl.AuthorServiceImpl;
import com.epam.tishkin.server.service.impl.BookServiceImpl;
import com.epam.tishkin.server.service.impl.BookmarkServiceImpl;
import com.epam.tishkin.server.service.impl.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LibraryServerConfig {

    @Bean
    public BookService bookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        return new BookServiceImpl(bookRepository, authorRepository);
    }

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository);
    }

    @Bean
    public BookmarkService bookmarkService(BookmarkRepository bookmarkRepository) {
        return new BookmarkServiceImpl(bookmarkRepository);
    }

    @Bean
    public AuthorService authorService(AuthorRepository authorRepository) {
        return new AuthorServiceImpl(authorRepository);
    }
}
