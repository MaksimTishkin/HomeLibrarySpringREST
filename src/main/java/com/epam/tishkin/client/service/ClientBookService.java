package com.epam.tishkin.client.service;

import com.epam.tishkin.models.Author;
import com.epam.tishkin.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ClientBookService {
    private static final String REST_URI = "http://localhost:8083";
    private final RestTemplate restTemplate;

    @Autowired
    public ClientBookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean addNewBook(String bookTitle, String ISBNumber, int publicationYear,
                              int pagesNumber, String bookAuthor) {
        Book book = restTemplate.postForObject(REST_URI + "/books/add",
                new Book(bookTitle, ISBNumber, publicationYear, pagesNumber, new Author(bookAuthor)), Book.class);
        return book != null;
    }

    public void deleteBook(String title, String authorName) {
        restTemplate.delete(REST_URI + "/books/delete/{title}/{authorName}",title, authorName);
    }

    public List<Book> searchBookByTitle(String title) {
        ResponseEntity<List<Book>> foundBooks = restTemplate
                .exchange(REST_URI + "/books/get-by-title/{title}", HttpMethod.GET,
                        null, new ParameterizedTypeReference<>(){}, title);
        return foundBooks.getBody();
    }

    public List<Book> searchBooksByAuthor(String bookAuthor) {
        ResponseEntity<List<Book>> foundBooks = restTemplate
                .exchange(REST_URI + "/books/get-by-author/{bookAuthor}", HttpMethod.GET,
                        null, new ParameterizedTypeReference<>(){}, bookAuthor);
        return foundBooks.getBody();
    }

    public Book searchBookByISBN(String isbn) {
        return restTemplate.getForObject(REST_URI + "/books/get-by-isbn/{isbn}", Book.class, isbn);
    }

    public List<Book> searchBooksByYearRange(int startYear, int finishYear) {
        ResponseEntity<List<Book>> foundBooks = restTemplate
                .exchange(REST_URI + "/books/get-by-years/{startYear}/{finishYear}", HttpMethod.GET,
                        null, new ParameterizedTypeReference<>(){}, startYear, finishYear);
        return foundBooks.getBody();
    }

    public List<Book> searchBookByYearPagesNumberAndTitle(int year, int pages, String title) {
        ResponseEntity<List<Book>> foundBooks = restTemplate
                .exchange(REST_URI + "/books/get-by-year-pages-title/{year}/{pages}/{title}", HttpMethod.GET,
                        null, new ParameterizedTypeReference<>(){}, year, pages, title);
        return foundBooks.getBody();
    }

    public Book findBookByFullTitle(String bookTitle) {
        return restTemplate.getForObject(REST_URI + "/books/get-by-full-title/{title}", Book.class, bookTitle);
    }

    //TODO: add a method for adding books from csv or json catalog
}
