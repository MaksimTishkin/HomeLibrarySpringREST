package com.epam.tishkin.server.service;

import com.epam.tishkin.model.Author;

public interface AuthorService {
    String addAuthor(Author author);
    String deleteAuthor(String name);
}
