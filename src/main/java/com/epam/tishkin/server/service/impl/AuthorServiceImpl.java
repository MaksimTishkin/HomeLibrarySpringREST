package com.epam.tishkin.server.service.impl;

import com.epam.tishkin.models.Author;
import com.epam.tishkin.server.repository.AuthorRepository;
import com.epam.tishkin.server.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public Author addAuthor(Author author) {
        Optional<Author> currentAuthor = authorRepository.findById(author.getName());
        if (currentAuthor.isPresent()) {
            return authorRepository.save(author);
        }
        return null;
    }

    @Override
    public void deleteAuthor(String authorName) {
        authorRepository.delete(new Author(authorName));
    }
}
