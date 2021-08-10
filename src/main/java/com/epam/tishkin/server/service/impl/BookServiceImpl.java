package com.epam.tishkin.server.service.impl;

import com.epam.tishkin.models.Author;
import com.epam.tishkin.models.Book;
import com.epam.tishkin.server.repository.BookRepository;
import com.epam.tishkin.server.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book addNewBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(String title, Author author) {
        bookRepository.deleteByTitleAndAuthor(title, author);
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleLike(title);
    }

    @Override
    public List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.findBooksByAuthorNameLike(authorName);
    }

    @Override
    public Book getBookByISBN(String isbn) {
        return bookRepository.findBookByISBNumber(isbn);
    }

    @Override
    public List<Book> getBooksByYearRange(int startYear, int finishYear) {
        return bookRepository.findBooksByYearBetween(startYear, finishYear);
    }

    @Override
    public List<Book> getBooksByYearPagesNumberAndTitle(int year, int pages, String title) {
        return bookRepository.findBooksByYearAndPagesNumberAndTitle(year, pages, title);
    }

    @Override
    public Book getBookByFullTitle(String bookTitle) {
        return bookRepository.findBookByTitle(bookTitle);
    }

    /*
    @Override
    public int addBooksFromCatalog(File file) {
        return libraryDAO.addBooksFromCatalog(file);
    }
    */
}
