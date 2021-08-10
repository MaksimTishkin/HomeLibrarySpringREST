package com.epam.tishkin.server.repository;

import com.epam.tishkin.models.Bookmark;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookmarkRepository extends CrudRepository<Bookmark, Integer> {
    boolean deleteByTitleAndLogin(String title);
    List<Bookmark> findBookmarksByLogin();
}
