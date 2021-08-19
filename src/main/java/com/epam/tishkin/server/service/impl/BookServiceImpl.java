package com.epam.tishkin.server.service.impl;

import com.epam.tishkin.models.Author;
import com.epam.tishkin.models.Book;
import com.epam.tishkin.models.Response;
import com.epam.tishkin.server.repository.AuthorRepository;
import com.epam.tishkin.server.repository.BookRepository;
import com.epam.tishkin.server.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
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
    public ResponseEntity<String> addNewBook(Book book) {
        if (bookRepository.findBookByISBNumber(book.getISBNumber()) != null) {
            throw new EntityExistsException("Book already exists: " + book.getTitle());
        }
        Optional<Author> currentAuthor = authorRepository.findById(book.getAuthor().getName());
        if (currentAuthor.isEmpty()) {
            authorRepository.save(book.getAuthor());
        }
        bookRepository.save(book);
        return ResponseEntity.ok("Book was added: " + book.getTitle());
    }

    @Override
    public ResponseEntity<String> deleteBook(String title) {
        Book currentBook = bookRepository.findBookByTitle(title);
        if (currentBook == null) {
            throw new EntityNotFoundException("Not found: " + title);
        }
        bookRepository.delete(currentBook);
        return ResponseEntity.ok("Book was deleted: " + title);
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
