package com.epam.tishkin.server.service;

import com.epam.tishkin.models.Author;
import org.springframework.http.ResponseEntity;

public interface AuthorService {
    String addAuthor(Author author);
    String deleteAuthor(String name);
}
