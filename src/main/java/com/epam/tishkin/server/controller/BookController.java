package com.epam.tishkin.server.controller;

import com.epam.tishkin.models.Book;
import com.epam.tishkin.server.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(value = "/add")
    public Book addBook(@RequestBody Book book) {
        Book foundBook = bookService.addNewBook(book);
        System.out.println(foundBook);
        return foundBook;
    }

    @GetMapping(value = "/get-by-title/{title}")
    public ResponseEntity<List<Book>> searchBookForTitle(@PathVariable(name = "title") String title) {
        List<Book> foundBooks = bookService.getBooksByTitle(title);
        System.out.println(foundBooks);
        return ResponseEntity.ok(foundBooks);
    }
}
