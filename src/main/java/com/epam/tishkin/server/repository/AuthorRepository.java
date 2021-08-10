package com.epam.tishkin.server.repository;

import com.epam.tishkin.models.Author;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, String> {
    void deleteByName(String name);
}
