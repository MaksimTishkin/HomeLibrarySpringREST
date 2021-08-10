package com.epam.tishkin.server.service;

import com.epam.tishkin.models.Author;

public interface AuthorService {
    Author addAuthor(Author author);
    void deleteAuthor(String name);
}
