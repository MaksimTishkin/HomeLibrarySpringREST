package com.epam.tishkin.server.service;

import com.epam.tishkin.models.Bookmark;

import java.util.List;

public interface BookmarkService {
    Bookmark addBookmark(Bookmark bookmark);
    boolean deleteBookmark(String title);
    List<Bookmark> getBookmarks();
}
