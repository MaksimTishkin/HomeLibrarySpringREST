package com.epam.tishkin.server.service.impl;

import com.epam.tishkin.models.Author;
import com.epam.tishkin.server.repository.AuthorRepository;
import com.epam.tishkin.server.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public ResponseEntity<String> addAuthor(Author author) {
        if (authorRepository.findById(author.getName()).isPresent()) {
            throw new EntityExistsException("Author already exists: " + author.getName());
        }
        authorRepository.save(author);
        return ResponseEntity.ok("Author was added: " + author.getName());
    }

    @Override
    public ResponseEntity<String> deleteAuthor(String authorName) {
        authorRepository.findById(authorName)
                .orElseThrow(() -> new EntityNotFoundException("Author not found: " + authorName));
        authorRepository.deleteById(authorName);
        return ResponseEntity.ok("Author was deleted: " + authorName);
    }
}
