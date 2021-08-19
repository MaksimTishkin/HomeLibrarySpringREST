package com.epam.tishkin.server.service.impl;

import com.epam.tishkin.models.Author;
import com.epam.tishkin.server.repository.AuthorRepository;
import com.epam.tishkin.server.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public ResponseEntity<String> addAuthor(Author author) {
        Optional<Author> currentAuthor = authorRepository.findById(author.getName());
        if (currentAuthor.isPresent()) {
            throw new EntityExistsException("Author already exists: " + author.getName());
        }
        authorRepository.save(author);
        return ResponseEntity.ok("Author was added: " + author.getName());
    }

    /*
    @Override
    public Author addAuthor(Author author) {
        Optional<Author> currentAuthor = authorRepository.findById(author.getName());
        if (currentAuthor.isPresent()) {
            return authorRepository.save(author);
        }
        return null;
    }
     */

    @Override
    public ResponseEntity<String> deleteAuthor(String authorName) {
        if (authorRepository.findById(authorName).isPresent()) {
            authorRepository.delete(new Author(authorName));
            return ResponseEntity.ok("Author was deleted: " + authorName);
        }
        throw new EntityNotFoundException("Not found: " + authorName);
    }
}
