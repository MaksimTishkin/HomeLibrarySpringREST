package com.epam.tishkin.server.service.impl;

import com.epam.tishkin.models.Bookmark;
import com.epam.tishkin.models.User;
import com.epam.tishkin.server.repository.BookmarkRepository;
import com.epam.tishkin.server.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;

    @Autowired
    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository) {
        this.bookmarkRepository = bookmarkRepository;
    }

    @Override
    public Bookmark addBookmark(Bookmark bookmark) {
        return bookmarkRepository.save(bookmark);
    }

    @Override
    public boolean deleteBookmark(String title) {
        return bookmarkRepository.deleteByTitle(title);
    }

    @Override
    public List<Bookmark> getBookmarks(User user) {
        return bookmarkRepository.findBookmarksByUser(user);
    }
}
