package com.epam.tishkin.server.controller;

import com.epam.tishkin.model.Author;
import com.epam.tishkin.server.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping(value = "/add/{authorName}")
    public ResponseEntity<String> addAuthor(@PathVariable(name = "authorName") String authorName) {
        return ResponseEntity.ok(authorService.addAuthor(new Author(authorName)));
    }

    @DeleteMapping (value = "/delete/{authorName}")
    public ResponseEntity<String> deleteAuthor(@PathVariable(name = "authorName") String authorName) {
        return ResponseEntity.ok(authorService.deleteAuthor(authorName));
    }
}
