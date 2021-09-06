package com.epam.tishkin.server.controller;

import com.epam.tishkin.model.Author;
import com.epam.tishkin.server.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addAuthor(@Valid @RequestBody Author author) {
        return ResponseEntity.ok(authorService.addAuthor(author));
    }

    @DeleteMapping (value = "/delete/{authorName}")
    public ResponseEntity<String> deleteAuthor(
            @PathVariable(name = "authorName") String authorName) {
        return ResponseEntity.ok(authorService.deleteAuthor(authorName));
    }
}
