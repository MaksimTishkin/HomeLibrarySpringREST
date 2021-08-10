package com.epam.tishkin.client.service;

import com.epam.tishkin.client.exceptions.AccessDeniedException;
import com.epam.tishkin.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ClientServiceREST {
    private static final String REST_URI = "http://localhost:8083";
    private static final String AUTHORIZATION_PROPERTY = "token";
    private final RestTemplate restTemplate;

    @Autowired
    public ClientServiceREST(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String authorization(String login, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("login", login);
        headers.set("password", password);
        ResponseEntity<String> response = restTemplate.exchange(REST_URI + "/authenticate", HttpMethod.POST,
                new HttpEntity<String>(headers), String.class);
        if (response.getStatusCodeValue() == HttpStatus.UNAUTHORIZED.value()) {
            return null;
        }
        return response.getBody();
    }

    public String getRole() {
        ResponseEntity<String> response = restTemplate.getForEntity(REST_URI + "users/get-role", String.class);
        return response.getBody();
    }

    public boolean addNewBook(String bookTitle, String ISBNumber, int publicationYear,
                              int pagesNumber, String bookAuthor, String jwt) throws AccessDeniedException {
        Book book = restTemplate.postForObject(REST_URI + "/books/add",
                new Book(bookTitle, ISBNumber, publicationYear, pagesNumber, new Author(bookAuthor)), Book.class);
        return book != null;
    }

    public boolean deleteBook(String title, String authorName, String jwt) throws AccessDeniedException {
        restTemplate.delete(REST_URI + "books/delete/{title}/{authorName}",title, authorName);
        return true;
    }

    public boolean addAuthor(String authorName, String jwt) throws AccessDeniedException {
        Bookmark bookmark = restTemplate.postForObject(REST_URI + "authors/add", new Author(authorName), Bookmark.class);
        return bookmark != null;

    }

    public boolean deleteAuthor(String authorName, String jwt) throws AccessDeniedException {
        restTemplate.delete(REST_URI + "authors/delete/{authorName}", authorName);
        return true;
    }

    /*
    public String addBooksFromCatalog(String filePath, String jwt) throws AccessDeniedException {
        final FileDataBodyPart filePart = new FileDataBodyPart("file", new File(filePath));
        FormDataMultiPart formDataMultiPart = new FormDataMultiPart();
        final FormDataMultiPart multipart = (FormDataMultiPart) formDataMultiPart.bodyPart(filePart);
        Response response = client
                .target(REST_URI)
                .path("books/add-from-catalog")
                .request(MediaType.APPLICATION_JSON)
                .cookie(AUTHORIZATION_PROPERTY, jwt)
                .post(Entity.entity(multipart, multipart.getMediaType()));
        if (response.getStatus() == 403) {
            throw new AccessDeniedException("access denied");
        }
        return response.readEntity(String.class);
    }
     */

    public List<Book> searchBookForTitle(String title, String jwt) throws AccessDeniedException {
        ResponseEntity<List<Book>> foundBooks = restTemplate
                .exchange(REST_URI + "/books/get-by-title/{title}", HttpMethod.GET,
                        null, new ParameterizedTypeReference<List<Book>>(){}, title);
        return foundBooks.getBody();
    }

    public List<Book> searchBooksForAuthor(String bookAuthor, String jwt) throws AccessDeniedException {
        ResponseEntity<List<Book>> foundBooks = restTemplate
                .exchange(REST_URI + "books/get-by-author/{bookAuthor}", HttpMethod.GET,
                        null, new ParameterizedTypeReference<List<Book>>(){}, bookAuthor);
        return foundBooks.getBody();
    }

    public Book searchBookForISBN(String isbn, String jwt) throws AccessDeniedException {
        return restTemplate.getForObject(REST_URI + "books/get-by-isbn/{isbn}", Book.class, isbn);
    }

    public List<Book> searchBooksByYearRange(int startYear, int finishYear, String jwt) throws AccessDeniedException {
        ResponseEntity<List<Book>> foundBooks = restTemplate
                .exchange(REST_URI + "books/get-by-years/{startYear}/{finishYear}", HttpMethod.GET,
                        null, new ParameterizedTypeReference<List<Book>>(){}, startYear, finishYear);
        return foundBooks.getBody();
    }

    public List<Book> searchBookByYearPagesNumberAndTitle(int year, int pages, String title, String jwt) throws AccessDeniedException {
        ResponseEntity<List<Book>> foundBooks = restTemplate
                .exchange(REST_URI + "books/get-by-year-pages-title/{year}/{pages}/{title}", HttpMethod.GET,
                        null, new ParameterizedTypeReference<List<Book>>(){}, year, pages, title);
        return foundBooks.getBody();
    }

    public Book findBookByFullTitle(String bookTitle, String jwt) throws AccessDeniedException {
        return restTemplate.getForObject(REST_URI + "books/get-by-full-title/{title}", Book.class, bookTitle);
    }

    public boolean addBookmark(String bookTitle, int page, String jwt) throws AccessDeniedException {
        Bookmark bookmark = restTemplate.postForObject(REST_URI + "bookmarks/add", new Bookmark(bookTitle, page), Bookmark.class);
        return bookmark != null;
    }

    public boolean deleteBookmark(String bookTitle, String jwt) throws AccessDeniedException {
        restTemplate.delete(REST_URI + "bookmarks/delete/{title}", bookTitle);
        return true;
    }

    public List<Bookmark> showBooksWithBookmarks(String jwt) throws AccessDeniedException {
        ResponseEntity<List<Bookmark>> foundBookmarks = restTemplate
                .exchange(REST_URI + "users/get-bookmarks", HttpMethod.GET,
                        null, new ParameterizedTypeReference<List<Bookmark>>(){});
        return foundBookmarks.getBody();
    }

    public boolean addUser(String login, String password, String jwt) throws AccessDeniedException {
        User libraryUser = restTemplate.postForObject(REST_URI + "users/add", new User(login, password, Role.VISITOR), User.class);
        return libraryUser != null;
    }

    public boolean blockUser(String login, String jwt) throws AccessDeniedException {
        restTemplate.delete(REST_URI + "users/delete/{login}", login);
        return true;
    }

    public List<String> showHistory(String jwt) throws AccessDeniedException {
        ResponseEntity<List<String>> history = restTemplate
                .exchange(REST_URI + "users/show-history", HttpMethod.GET,
                        null, new ParameterizedTypeReference<List<String>>(){});
        return history.getBody();
    }
}
