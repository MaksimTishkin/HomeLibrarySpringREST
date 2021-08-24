package com.epam.tishkin.server.repository;

import com.epam.tishkin.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    String findUserRoleByLogin(String login);
}
