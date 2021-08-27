package com.epam.tishkin.server.service;

import com.epam.tishkin.models.Bookmark;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface BookmarkService {
    String addBookmark(String title, int page);
    String deleteBookmark(String title);
    List<Bookmark> showBookmarks();
}
