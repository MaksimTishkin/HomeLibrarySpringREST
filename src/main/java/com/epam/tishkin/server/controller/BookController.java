package com.epam.tishkin.server.controller;

import com.epam.tishkin.model.Author;
import com.epam.tishkin.model.Book;
import com.epam.tishkin.server.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(value = "/add/{bookTitle}/{ISBNumber}/{publicationYear}/{pagesNumber}/{bookAuthor}")
    public ResponseEntity<String> addBook(
            @PathVariable(name = "bookTitle") String title,
            @PathVariable(name = "ISBNumber") String isbn,
            @PathVariable(name = "publicationYear") int year,
            @PathVariable(name = "pagesNumber") int pages,
            @PathVariable(name = "bookAuthor") String authorName) {
        Book book = new Book(title, isbn, year, pages, new Author(authorName));
        return ResponseEntity.ok(bookService.addNewBook(book));
    }

    @DeleteMapping(value = "/delete/{title}")
    public ResponseEntity<String> deleteBook(
            @PathVariable(name = "title") String title) {
    return ResponseEntity.ok(bookService.deleteBook(title));
    }

    @GetMapping(value = "/get-by-title/{title}")
    public ResponseEntity<List<Book>> searchBooksByTitle(@PathVariable(name = "title") String title) {
        List<Book> foundBooks = bookService.getBooksByTitle(title);
        return ResponseEntity.ok(foundBooks);
    }

    @GetMapping(value = "/get-by-author/{bookAuthor}")
    public ResponseEntity<List<Book>> searchBooksByAuthor(@PathVariable(name = "bookAuthor") String boolAuthor) {
        List<Book> foundBooks = bookService.getBooksByAuthor(boolAuthor);
        return ResponseEntity.ok(foundBooks);
    }

    @GetMapping(value = "/get-by-isbn/{isbn}")
    public ResponseEntity<Book> searchBookByISBN(@PathVariable(name = "isbn") String isbn) {
        Book foundBook = bookService.getBookByISBN(isbn);
        return ResponseEntity.ok(foundBook);
    }

    @GetMapping(value = "/get-by-years/{startYear}/{finishYear}")
    public ResponseEntity<List<Book>> searchByYearsRange(
            @PathVariable(name = "startYear") int startYear,
            @PathVariable(name = "finishYear") int finishYear) {
        List<Book> foundBooks = bookService.getBooksByYearRange(startYear, finishYear);
        return ResponseEntity.ok(foundBooks);
    }

    @GetMapping(value = "/get-by-year-pages-title/{year}/{pages}/{title}")
    public ResponseEntity<List<Book>> searchByYearAndPagesAndTitle(
            @PathVariable(name = "year") int year,
            @PathVariable(name = "pages") int pages,
            @PathVariable(name = "title") String title) {
        List<Book> foundBooks = bookService.getBooksByYearPagesNumberAndTitle(year, pages, title);
        return ResponseEntity.ok(foundBooks);
    }

    @GetMapping(value = "/get-by-full-title/{title}")
    public Book searchBookByFullTitle(@PathVariable(name = "title") String title) {
        return bookService.getBookByFullTitle(title);
    }

    @PostMapping(value = "/add-from-catalog")
    public ResponseEntity<String> addBooksFromCatalog(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(bookService.addBooksFromCatalog(file));
    }
}
