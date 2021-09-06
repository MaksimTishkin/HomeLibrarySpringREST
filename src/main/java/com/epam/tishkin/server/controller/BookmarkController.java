package com.epam.tishkin.server.controller;

import com.epam.tishkin.model.Bookmark;
import com.epam.tishkin.server.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/bookmarks")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @Autowired
    public BookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    @PostMapping(value = "/add")
    public ResponseEntity<String> addBookmark(@Valid @RequestBody Bookmark bookmark) {
        return ResponseEntity.ok(bookmarkService.addBookmark(bookmark));
    }

    @DeleteMapping(value = "/delete/{title}")
    public ResponseEntity<String> deleteBookmark(@PathVariable(name = "title") String title) {
        return ResponseEntity.ok(bookmarkService.deleteBookmark(title));
    }

    @GetMapping(value = "/get")
    public ResponseEntity<List<Bookmark>> getBookmarks() {
        return ResponseEntity.ok(bookmarkService.showBookmarks());
    }
}
