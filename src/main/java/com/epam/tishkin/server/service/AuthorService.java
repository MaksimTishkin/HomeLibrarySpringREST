package com.epam.tishkin.server.service;

import com.epam.tishkin.models.Author;
import org.springframework.http.ResponseEntity;

public interface AuthorService {
    ResponseEntity<String> addAuthor(Author author);
    ResponseEntity<String> deleteAuthor(String name);
}
