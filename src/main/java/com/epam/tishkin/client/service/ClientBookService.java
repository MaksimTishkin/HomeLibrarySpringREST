package com.epam.tishkin.client.service;

import com.epam.tishkin.client.utils.JwtHeadersUtil;
import com.epam.tishkin.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ClientBookService {
    private static final String REST_URI = "http://localhost:8088";
    private final RestTemplate restTemplate;

    @Autowired
    public ClientBookService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String addNewBook(String bookTitle, String ISBNumber, int publicationYear,
                             int pagesNumber, String bookAuthor) {
        try {
            HttpHeaders headers = JwtHeadersUtil.getHeadersCookieWithJwt();
            ResponseEntity<String> response = restTemplate.postForEntity(
                    REST_URI + "/books/add/{bookTitle}/{ISBNumber}/{publicationYear}/{pagesNumber}/{bookAuthor}",
                    new HttpEntity<String>(headers),
                    String.class,
                    bookTitle, ISBNumber, publicationYear, pagesNumber, bookAuthor);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return "Ops.... something is wrong " + e.getStatusCode();
        }
    }

    public String deleteBook(String title) {
        try {
            HttpHeaders headers = JwtHeadersUtil.getHeadersCookieWithJwt();
            ResponseEntity<String> response = restTemplate.exchange(REST_URI + "/books/delete/{title}",
                    HttpMethod.DELETE, new HttpEntity<String>(headers), String.class, title);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            return "Ops.... something is wrong " + e.getStatusCode();
        }
    }

    public List<Book> searchBookByTitle(String title) {
        HttpHeaders headers = JwtHeadersUtil.getHeadersCookieWithJwt();
        ResponseEntity<List<Book>> foundBooks = restTemplate
                .exchange(REST_URI + "/books/get-by-title/{title}", HttpMethod.GET,
                        new HttpEntity<>(headers), new ParameterizedTypeReference<>(){}, title);
        return foundBooks.getBody();
    }

    public List<Book> searchBooksByAuthor(String bookAuthor) {
        HttpHeaders headers = JwtHeadersUtil.getHeadersCookieWithJwt();
        ResponseEntity<List<Book>> foundBooks = restTemplate
                .exchange(REST_URI + "/books/get-by-author/{bookAuthor}", HttpMethod.GET,
                        new HttpEntity<>(headers), new ParameterizedTypeReference<>(){}, bookAuthor);
        return foundBooks.getBody();
    }

    public Book searchBookByISBN(String isbn) {
        HttpHeaders headers = JwtHeadersUtil.getHeadersCookieWithJwt();
        ResponseEntity<Book> response = restTemplate.exchange(REST_URI + "/books/get-by-isbn/{isbn}", HttpMethod.GET,
                new HttpEntity<String>(headers), Book.class, isbn);
        return response.getBody();
    }

    public List<Book> searchBooksByYearRange(int startYear, int finishYear) {
        HttpHeaders headers = JwtHeadersUtil.getHeadersCookieWithJwt();
        ResponseEntity<List<Book>> foundBooks = restTemplate
                .exchange(REST_URI + "/books/get-by-years/{startYear}/{finishYear}", HttpMethod.GET,
                        new HttpEntity<>(headers), new ParameterizedTypeReference<>(){}, startYear, finishYear);
        return foundBooks.getBody();
    }

    public List<Book> searchBookByYearPagesNumberAndTitle(int year, int pages, String title) {
        HttpHeaders headers = JwtHeadersUtil.getHeadersCookieWithJwt();
        ResponseEntity<List<Book>> foundBooks = restTemplate
                .exchange(REST_URI + "/books/get-by-year-pages-title/{year}/{pages}/{title}", HttpMethod.GET,
                        new HttpEntity<>(headers), new ParameterizedTypeReference<>(){}, year, pages, title);
        return foundBooks.getBody();
    }

    public Book findBookByFullTitle(String bookTitle) {
        HttpHeaders headers = JwtHeadersUtil.getHeadersCookieWithJwt();
        ResponseEntity<Book> foundBook = restTemplate.exchange(REST_URI + "/books/get-by-full-title/{title}",
                HttpMethod.GET, new HttpEntity<>(headers), Book.class, bookTitle);
        return foundBook.getBody();
    }

    //TODO: add a method for adding books from csv or json catalog
}
