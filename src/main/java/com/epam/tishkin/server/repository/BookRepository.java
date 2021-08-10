package com.epam.tishkin.server.repository;

import com.epam.tishkin.models.Author;
import com.epam.tishkin.models.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer> {
    void deleteByTitleAndAuthor(String title, Author author);
    List<Book> findByTitleLike(String partName);
    List<Book> findBooksByAuthorNameLike(String name);
    Book findBookByISBNumber(String isbn);
    List<Book> findBooksByYearBetween(int startYear, int finishYear);
    List<Book> findBooksByYearAndPagesNumberAndTitle(int year, int pages, String title);
    Book findBookByTitle(String title);
}
