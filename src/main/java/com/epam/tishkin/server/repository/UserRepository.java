package com.epam.tishkin.server.repository;

import com.epam.tishkin.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    User findByLoginAndPassword(String login, String password);
    boolean deleteByLogin(String login);
    String findUserRoleByLogin(String login);
}
