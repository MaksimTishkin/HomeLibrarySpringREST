package com.epam.tishkin.server.repository;

import com.epam.tishkin.models.Bookmark;
import com.epam.tishkin.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends CrudRepository<Bookmark, Integer> {
    boolean deleteByTitle(String title);
    List<Bookmark> findBookmarksByUser(User user);
}
