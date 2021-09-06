package com.epam.tishkin.server.config;

import com.epam.tishkin.server.advice.HistoryWritingAspect;
import com.epam.tishkin.server.repository.AuthorRepository;
import com.epam.tishkin.server.repository.BookRepository;
import com.epam.tishkin.server.repository.BookmarkRepository;
import com.epam.tishkin.server.repository.UserRepository;
import com.epam.tishkin.server.security.jwt.JwtProvider;
import com.epam.tishkin.server.service.*;
import com.epam.tishkin.server.service.impl.*;
import com.epam.tishkin.server.manager.HistoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @DependsOn({"bookmarkRepository", "bookRepository", "userRepository"})
    public BookmarkService bookmarkService(BookmarkRepository bookmarkRepository,
                                           BookRepository bookRepository,
                                           UserRepository userRepository) {
        return new BookmarkServiceImpl(bookmarkRepository, bookRepository, userRepository);
    }

    @Bean
    @DependsOn("authorRepository")
    public AuthorService authorService(AuthorRepository authorRepository) {
        return new AuthorServiceImpl(authorRepository);
    }

    @Bean
    @DependsOn({"userRepository", "historyManager", "passwordEncoder"})
    public AdminService adminService(UserRepository userRepository,
                                     HistoryManager historyManager,
                                     PasswordEncoder encoder) {
        return new AdminServiceImpl(userRepository, historyManager, encoder);
    }

    @Bean
    public HistoryManager historyManager() {
        return new HistoryManager();
    }

    @Bean
    @DependsOn("historyManager")
    public HistoryWritingAspect historyWritingAspect(HistoryManager historyManager) {
        return new HistoryWritingAspect(historyManager);
    }
}
