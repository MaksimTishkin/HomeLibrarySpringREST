package com.epam.tishkin.server.repository;

import com.epam.tishkin.models.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer> {
    List<Book> findByTitleLike(String partName);
}
