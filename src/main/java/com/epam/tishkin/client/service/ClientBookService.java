package com.epam.tishkin.client.service;

import com.epam.tishkin.client.exception.CustomResponseException;
import com.epam.tishkin.client.util.JwtHeadersUtil;
import com.epam.tishkin.model.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public String addBooksFromCatalog(String filePath) {
        File file = new File(filePath);
        FileSystemResource resource = new FileSystemResource(file);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);
        HttpHeaders headers = jwtHeaders.getHeadersCookieWithJwt();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(REST_URI + "/books/add-from-catalog",
                    HttpMethod.POST, requestEntity, String.class);
            return response.getBody();
        } catch (CustomResponseException e) {
            return e.getMessage();
        }
    }
}
