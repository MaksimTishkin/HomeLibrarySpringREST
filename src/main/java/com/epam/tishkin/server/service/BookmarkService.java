package com.epam.tishkin.server.service;

import com.epam.tishkin.model.Bookmark;

import java.util.List;

public interface BookmarkService {
    String addBookmark(Bookmark bookmark);
    String deleteBookmark(String title);
    List<Bookmark> showBookmarks();
}
