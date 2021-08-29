package com.epam.tishkin.server.repository;

import com.epam.tishkin.model.Bookmark;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends CrudRepository<Bookmark, Integer> {
    Optional<Bookmark> findByTitleAndUserLogin(String title, String userName);
    List<Bookmark> findByUserLogin(String userName);
}
