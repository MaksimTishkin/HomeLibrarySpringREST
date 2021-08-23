package com.epam.tishkin.server.repository;

import com.epam.tishkin.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByLogin(String login);
    User findByLoginAndPassword(String login, String password);
    String findUserRoleByLogin(String login);
}
