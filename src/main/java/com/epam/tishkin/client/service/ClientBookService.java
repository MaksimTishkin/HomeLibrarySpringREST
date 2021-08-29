package com.epam.tishkin.client.service;

import com.epam.tishkin.client.exception.CustomResponseException;
import com.epam.tishkin.client.util.JwtHeadersUtil;
import com.epam.tishkin.model.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ClientBookService {
    @Value("${rest.uri}")
    private String REST_URI;
    private final RestTemplate restTemplate;
    private final JwtHeadersUtil jwtHeaders;
    Logger logger = LogManager.getLogger(ClientBookService.class);

    @Autowired
    public ClientBookService(RestTemplate restTemplate, JwtHeadersUtil jwtHeaders) {
        this.restTemplate = restTemplate;
        this.jwtHeaders = jwtHeaders;
    }

    public String addNewBook(String bookTitle, String ISBNumber, int publicationYear,
                             int pagesNumber, String bookAuthor) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    REST_URI + "/books/add/{bookTitle}/{ISBNumber}/{publicationYear}/{pagesNumber}/{bookAuthor}",
                    new HttpEntity<String>(headers),
                    String.class,
                    bookTitle, ISBNumber, publicationYear, pagesNumber, bookAuthor);
            return response.getBody();
        } catch (CustomResponseException e) {
            return e.getMessage();
        }
    }

    public String deleteBook(String title) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        try {
            ResponseEntity<String> response = restTemplate.exchange(REST_URI + "/books/delete/{title}",
                    HttpMethod.DELETE, new HttpEntity<String>(headers), String.class, title);
            return response.getBody();
        } catch (CustomResponseException e) {
            return e.getMessage();
        }
    }

    public List<Book> searchBookByTitle(String title) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        List<Book> books = null;
        try {
            ResponseEntity<List<Book>> foundBooks = restTemplate
                    .exchange(REST_URI + "/books/get-by-title/{title}", HttpMethod.GET,
                            new HttpEntity<>(headers), new ParameterizedTypeReference<>(){}, title);
            books = foundBooks.getBody();
        } catch (CustomResponseException e) {
            logger.error(e.getMessage());
        }
        return books;
    }

    public List<Book> searchBooksByAuthor(String bookAuthor) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        List<Book> books = null;
        try {
            ResponseEntity<List<Book>> foundBooks = restTemplate
                    .exchange(REST_URI + "/books/get-by-author/{bookAuthor}", HttpMethod.GET,
                            new HttpEntity<>(headers), new ParameterizedTypeReference<>(){}, bookAuthor);
            books = foundBooks.getBody();
        } catch (CustomResponseException e) {
            logger.error(e.getMessage());
        }
        return books;
    }

    public Book searchBookByISBN(String isbn) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        Book book = null;
        try {
            ResponseEntity<Book> response = restTemplate.exchange(REST_URI + "/books/get-by-isbn/{isbn}", HttpMethod.GET,
                    new HttpEntity<String>(headers), Book.class, isbn);
            book = response.getBody();
        } catch (CustomResponseException e) {
            logger.error(e.getMessage());
        }
        return book;
    }

    public List<Book> searchBooksByYearRange(int startYear, int finishYear) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        List<Book> books = null;
        try {
            ResponseEntity<List<Book>> response = restTemplate
                    .exchange(REST_URI + "/books/get-by-years/{startYear}/{finishYear}", HttpMethod.GET,
                            new HttpEntity<>(headers), new ParameterizedTypeReference<>(){}, startYear, finishYear);
            books = response.getBody();
        } catch (CustomResponseException e) {
            logger.error(e.getMessage());
        }
        return books;
    }

    public List<Book> searchBookByYearPagesNumberAndTitle(int year, int pages, String title) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        List<Book> books = null;
        try {
            ResponseEntity<List<Book>> response = restTemplate
                    .exchange(REST_URI + "/books/get-by-year-pages-title/{year}/{pages}/{title}", HttpMethod.GET,
                            new HttpEntity<>(headers), new ParameterizedTypeReference<>(){}, year, pages, title);
            books = response.getBody();
        } catch (CustomResponseException e) {
            logger.error(e.getMessage());
        }
        return books;
    }

    //TODO: add a method for adding books from csv or json catalog
}
