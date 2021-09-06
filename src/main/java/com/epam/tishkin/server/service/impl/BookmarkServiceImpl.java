package com.epam.tishkin.server.service.impl;

import com.epam.tishkin.model.Book;
import com.epam.tishkin.model.Bookmark;
import com.epam.tishkin.model.User;
import com.epam.tishkin.server.repository.BookRepository;
import com.epam.tishkin.server.repository.BookmarkRepository;
import com.epam.tishkin.server.repository.UserRepository;
import com.epam.tishkin.server.service.BookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

public class BookmarkServiceImpl implements BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository,
                               BookRepository bookRepository,
                               UserRepository userRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    public String addBookmark(Bookmark bookmark) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findById(currentUsername);
        if (user.isPresent()) {
            Book book = bookRepository.findBookByTitle(bookmark.getTitle())
                    .orElseThrow(() -> new EntityNotFoundException("Book not found: " + bookmark.getTitle()));
            if (book.getPagesNumber() <= bookmark.getPage()) {
                throw new EntityNotFoundException("The page with this number is not in the book: " + bookmark.getTitle());
            }
            if (bookmarkRepository.findByTitleAndUserLogin(bookmark.getTitle(), currentUsername).isPresent()) {
                throw new EntityExistsException("Bookmark already exists: " + bookmark.getTitle());
            }
            bookmark.setUser(user.get());
            bookmarkRepository.save(bookmark);
            return "Bookmark was added: " + bookmark.getTitle();
        }
        throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED);
    }

    @Override
    public String deleteBookmark(String title) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        Bookmark bookmark = bookmarkRepository.findByTitleAndUserLogin(title, currentUsername)
                .orElseThrow(() -> new EntityNotFoundException("Bookmark not found: " + title));
        bookmarkRepository.delete(bookmark);
        return "Bookmark was deleted: " + title;
    }

    @Override
    public List<Bookmark> showBookmarks() {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return bookmarkRepository.findByUserLogin(currentUsername);
    }
}
