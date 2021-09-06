package com.epam.tishkin.server.repository;

import com.epam.tishkin.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    User findUserByLogin(String login);
}
