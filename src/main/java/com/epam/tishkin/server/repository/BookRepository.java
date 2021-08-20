package com.epam.tishkin.server.repository;

import com.epam.tishkin.models.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Integer> {
    List<Book> findByTitleContaining(String partName);
    List<Book> findBooksByAuthorNameContaining(String name);
    Book findBookByISBNumber(String isbn);
    List<Book> findBooksByPublicationYearBetween(int startYear, int finishYear);
    List<Book> findBooksByPublicationYearAndPagesNumberAndTitle(int year, int pages, String title);
    Book findBookByTitle(String title);
}
