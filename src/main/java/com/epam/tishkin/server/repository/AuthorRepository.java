package com.epam.tishkin.server.repository;

import com.epam.tishkin.models.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends CrudRepository<Author, String> {
    void deleteByName(String name);
}
