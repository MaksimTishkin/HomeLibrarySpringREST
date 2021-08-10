package com.epam.tishkin.server.controller;

import com.epam.tishkin.models.Author;
import com.epam.tishkin.server.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Author addAuthor(@RequestBody Author author) {
        return authorService.addAuthor(author);
    }

    @GetMapping(value = "/delete/{authorName}")
    public void deleteAuthor(@PathVariable(name = "authorName") String authorName) {
        authorService.deleteAuthor(authorName);
    }
}
