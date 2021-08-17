package com.epam.tishkin.server.controller;

import com.epam.tishkin.models.Bookmark;
import com.epam.tishkin.models.Role;
import com.epam.tishkin.models.User;
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

    @PostMapping(value = "/add")
    public Bookmark addBookmark(@RequestBody Bookmark bookmark) {
        return bookmarkService.addBookmark(bookmark);
    }

    @DeleteMapping(value = "/delete/{title}")
    public void deleteBookmark(@PathVariable(name = "title") String title) {
        bookmarkService.deleteBookmark(title);
    }

    @GetMapping(value = "/get-bookmarks")
    public ResponseEntity<List<Bookmark>> getBookmarks() {
        User user = new User("maxim", "555", Role.VISITOR);
        List<Bookmark> foundBookmarks = bookmarkService.getBookmarks(user);
        return ResponseEntity.ok(foundBookmarks);
    }
}
