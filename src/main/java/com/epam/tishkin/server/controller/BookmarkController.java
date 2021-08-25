package com.epam.tishkin.server.controller;

import com.epam.tishkin.models.Bookmark;
import com.epam.tishkin.server.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping(value = "/add/{title}/{page}")
    public ResponseEntity<String> addBookmark(
            @PathVariable(name = "title") String title,
            @PathVariable(name = "page") int page) {
        return bookmarkService.addBookmark(title, page);
    }

    @DeleteMapping(value = "/delete/{title}")
    public ResponseEntity<String> deleteBookmark(@PathVariable(name = "title") String title) {
        return bookmarkService.deleteBookmark(title);
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<Bookmark>> getBookmarks() {
        return ResponseEntity.ok(bookmarkService.getBookmarks());
    }
}
