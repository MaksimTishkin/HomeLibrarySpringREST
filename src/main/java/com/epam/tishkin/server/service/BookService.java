package com.epam.tishkin.server.service;

import com.epam.tishkin.models.Book;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookService {
    String addNewBook(Book book);
    String deleteBook(String title);
    List<Book> getBooksByTitle(String title);
    List<Book> getBooksByAuthor(String authorName);
    Book getBookByISBN(String isbn);
    List<Book> getBooksByYearRange(int initialYear, int finalYear);
    List<Book> getBooksByYearPagesNumberAndTitle(int year, int pages, String title);
    Book getBookByFullTitle(String bookTitle);
    //int addBooksFromCatalog(File file);
}
