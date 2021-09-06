package com.epam.tishkin.client.service;

import com.epam.tishkin.client.exception.CustomResponseException;
import com.epam.tishkin.client.manager.JwtHeadersManager;
import com.epam.tishkin.model.Author;
import com.epam.tishkin.model.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;

public class BookService {
    private static final String REST_URL = "http://localhost:8088/books";
    private static final String ADD_BOOK_URI = REST_URL + "/add";
    private static final String DELETE_BOOK_URI = REST_URL + "/delete";
    private static final String GET_BOOKS_BY_TITLE_URI = REST_URL + "/get-by-title";
    private static final String GET_BOOKS_BY_AUTHOR_URI = REST_URL + "/get-by-author";
    private static final String GET_BOOK_BY_ISBN_URI = REST_URL + "/get-by-isbn";
    private static final String GET_BOOKS_BY_YEARS_URI = REST_URL + "/get-by-years";
    private static final String GET_BOOKS_BY_YEAR_PAGE_TITLE_URI = REST_URL + "/get-by-year-pages-title";
    private static final String ADD_BOOKS_FROM_CATALOG_URI = REST_URL + "/add-from-catalog";
    private final RestTemplate restTemplate;
    private final JwtHeadersManager jwtHeaders;
    Logger logger = LogManager.getLogger(BookService.class);

    @Autowired
    public BookService(RestTemplate restTemplate, JwtHeadersManager jwtHeaders) {
        this.restTemplate = restTemplate;
        this.jwtHeaders = jwtHeaders;
    }

    public String addNewBook(String bookTitle, String ISBNumber, int publicationYear,
                             int pagesNumber, String bookAuthor) {
        Book book = new Book(bookTitle, ISBNumber, publicationYear, pagesNumber, new Author(bookAuthor));
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    ADD_BOOK_URI,
                    new HttpEntity<>(book, headers),
                    String.class);
            return response.getBody();
        } catch (CustomResponseException e) {
            return e.getMessage();
        }
    }

    public String deleteBook(String title) {
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        try {
            ResponseEntity<String> response = restTemplate.exchange(DELETE_BOOK_URI + "/{title}",
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
                    .exchange(GET_BOOKS_BY_TITLE_URI + "/{title}", HttpMethod.GET,
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
                    .exchange(GET_BOOKS_BY_AUTHOR_URI + "/{bookAuthor}", HttpMethod.GET,
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
            ResponseEntity<Book> response = restTemplate.exchange(GET_BOOK_BY_ISBN_URI + "/{isbn}",
                    HttpMethod.GET,
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
                    .exchange(GET_BOOKS_BY_YEARS_URI + "/{startYear}" + "/{finishYear}", HttpMethod.GET,
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
                    .exchange(GET_BOOKS_BY_YEAR_PAGE_TITLE_URI + "/{year}/{pages}/{title}", HttpMethod.GET,
                            new HttpEntity<>(headers), new ParameterizedTypeReference<>(){}, year, pages, title);
            books = response.getBody();
        } catch (CustomResponseException e) {
            logger.error(e.getMessage());
        }
        return books;
    }

    public String addBooksFromCatalog(String filePath) {
        File file = new File(filePath);
        FileSystemResource resource = new FileSystemResource(file);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(ADD_BOOKS_FROM_CATALOG_URI,
                    HttpMethod.POST, requestEntity, String.class);
            return response.getBody();
        } catch (CustomResponseException e) {
            return e.getMessage();
        }
    }
}
