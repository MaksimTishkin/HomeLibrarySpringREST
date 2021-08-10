package com.epam.tishkin.server.service;

import com.epam.tishkin.models.Book;
import com.epam.tishkin.models.Bookmark;
import com.epam.tishkin.models.User;

import java.io.File;
import java.util.List;

public interface BookService {
    User authorization(String login, String password);
    boolean addNewBook(Book book);
    boolean deleteBook(String authorName, String bookTitle);
    boolean addAuthor(String authorName);
    boolean deleteAuthor(String authorName);
    //int addBooksFromCatalog(File file);
    List<Book> getBooksByTitle(String title);
    List<Book> getBooksByAuthor(String authorName);
    Book getBookByISBN(String isbn);
    List<Book> getBooksByYearRange(int initialYear, int finalYear);
    List<Book> getBooksByYearPagesNumberAndTitle(int year, int pages, String title);
    Book getBookByFullTitle(String bookTitle);
    boolean addBookmark(Bookmark bookmark, String login);
    boolean deleteBookmark(String bookTitle, String login);
    List<Bookmark> getBookmarks(String login);
    boolean addNewUser(User libraryUser);
    boolean deleteUser(String login);
    //List<String> showHistory();
}
