package com.epam.tishkin.server.service;

import com.epam.tishkin.models.Bookmark;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookmarkService {
    ResponseEntity<String> addBookmark(String title, int page);
    ResponseEntity<String> deleteBookmark(String title);
    List<Bookmark> getBookmarks();
}
