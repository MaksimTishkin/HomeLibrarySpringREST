package com.epam.tishkin.server.controller;

import com.epam.tishkin.models.Author;
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

    @PostMapping(value = "/add")
    public ResponseEntity<String> addAuthor(@RequestBody Author author) {
        return authorService.addAuthor(author);
    }

    @DeleteMapping (value = "/delete/{authorName}")
    public ResponseEntity<String> deleteAuthor(@PathVariable(name = "authorName") String authorName) {
        return authorService.deleteAuthor(authorName);
    }
}
