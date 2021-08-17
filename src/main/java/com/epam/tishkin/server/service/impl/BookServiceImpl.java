package com.epam.tishkin.server.service.impl;

import com.epam.tishkin.models.Author;
import com.epam.tishkin.models.Book;
import com.epam.tishkin.server.repository.AuthorRepository;
import com.epam.tishkin.server.repository.BookRepository;
import com.epam.tishkin.server.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public Book addNewBook(Book book) {
        Optional<Author> author = authorRepository.findById(book.getAuthor().getName());
        author.ifPresent(book::setAuthor);
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(String title, Author author) {
        bookRepository.deleteByTitleAndAuthor(title, author);
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContaining(title);
    }

    @Override
    public List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.findBooksByAuthorNameContaining(authorName);
    }

    @Override
    public Book getBookByISBN(String isbn) {
        return bookRepository.findBookByISBNumber(isbn);
    }

    @Override
    public List<Book> getBooksByYearRange(int startYear, int finishYear) {
        return bookRepository.findBooksByPublicationYearBetween(startYear, finishYear);
    }

    @Override
    public List<Book> getBooksByYearPagesNumberAndTitle(int publicationYear, int pages, String title) {
        return bookRepository.findBooksByPublicationYearAndPagesNumberAndTitle(publicationYear, pages, title);
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
