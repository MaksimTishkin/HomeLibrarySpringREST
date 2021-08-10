package com.epam.tishkin.server.service.impl;

import com.epam.tishkin.models.Book;
import com.epam.tishkin.models.Bookmark;
import com.epam.tishkin.models.User;
import com.epam.tishkin.server.dao.LibraryDAO;
import com.epam.tishkin.server.repository.BookRepository;
import com.epam.tishkin.server.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BookServiceImpl implements BookService {
    private final LibraryDAO libraryDAO;
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(LibraryDAO libraryDAO, BookRepository bookRepository) {
        this.libraryDAO = libraryDAO;
        this.bookRepository = bookRepository;
    }

    @Override
    public Book addNewBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public User authorization(String login, String password) {
        return libraryDAO.userAuthorization(login, password);
    }

    @Override
    public boolean deleteBook(String authorName, String bookTitle) {
        return libraryDAO.deleteBook(authorName, bookTitle);
    }

    @Override
    public boolean addAuthor(String authorName) {
        return libraryDAO.addAuthor(authorName);
    }

    @Override
    public boolean deleteAuthor(String authorName) {
        return libraryDAO.deleteAuthor(authorName);
    }
/*
    @Override
    public int addBooksFromCatalog(File file) {
        return libraryDAO.addBooksFromCatalog(file);
    }
 */

    @Override
    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleLike(title);
    }

    @Override
    public List<Book> getBooksByAuthor(String authorName) {
        return libraryDAO.getBooksByAuthor(authorName);
    }

    @Override
    public Book getBookByISBN(String isbn) {
        return libraryDAO.getBookByISBN(isbn);
    }

    @Override
    public List<Book> getBooksByYearRange(int startYear, int finishYear) {
        return libraryDAO.getBooksByYearRange(startYear, finishYear);
    }

    @Override
    public List<Book> getBooksByYearPagesNumberAndTitle(int year, int pages, String title) {
        return libraryDAO.getBooksByYearPagesNumberAndTitle(year, pages, title);
    }

    @Override
    public Book getBookByFullTitle(String bookTitle) {
        return libraryDAO.getBookByFullTitle(bookTitle);
    }

    @Override
    public boolean addBookmark(Bookmark bookmark, String login) {
        return libraryDAO.addBookmark(bookmark, login);
    }

    @Override
    public boolean deleteBookmark(String bookTitle, String login) {
        return libraryDAO.deleteBookmark(bookTitle, login);
    }

    @Override
    public List<Bookmark> getBookmarks(String login) {
        return  libraryDAO.getBookmarks(login);
    }

    @Override
    public boolean addNewUser(User libraryUser) {
        return libraryDAO.addUser(libraryUser);
    }

    @Override
    public boolean deleteUser(String login) {
        return libraryDAO.blockUser(login);
    }

    /*
    @Override
    public List<String> showHistory() {
        return HistoryManager.read();
    }
    */
}
