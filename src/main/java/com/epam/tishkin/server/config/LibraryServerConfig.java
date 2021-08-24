package com.epam.tishkin.server.config;

import com.epam.tishkin.server.repository.AuthorRepository;
import com.epam.tishkin.server.repository.BookRepository;
import com.epam.tishkin.server.repository.BookmarkRepository;
import com.epam.tishkin.server.repository.UserRepository;
import com.epam.tishkin.server.security.jwt.JwtProvider;
import com.epam.tishkin.server.service.*;
import com.epam.tishkin.server.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
public class LibraryServerConfig {
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public LibraryServerConfig(JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    @Bean
    @DependsOn({"bookRepository", "authorRepository"})
    public BookService bookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        return new BookServiceImpl(bookRepository, authorRepository);
    }

    @Bean
    @DependsOn("userRepository")
    public UserService userService(UserRepository userRepository) {
        return new UserServiceImpl(userRepository, authenticationManager, jwtProvider);
    }

    @Bean
    @DependsOn("bookmarkRepository")
    public BookmarkService bookmarkService(BookmarkRepository bookmarkRepository) {
        return new BookmarkServiceImpl(bookmarkRepository);
    }

    @Bean
    public AuthorService authorService(AuthorRepository authorRepository) {
        return new AuthorServiceImpl(authorRepository);
    }

    @Bean
    public AdminService adminService(UserRepository userRepository) {
        return new AdminServiceImpl(userRepository);
    }
}
