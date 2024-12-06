package com.example.javaproject.dao;

import com.example.javaproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by login and password
    User findByLoginAndMotDePasse(String login, String motDePasse);
    // SELECT * FROM User WHERE login = ? AND motDePasse = ?

    // Check if a login already exists
    boolean existsByLogin(String login);

    // Find a user by login
    User findByLogin(String login);
    // SELECT * FROM User WHERE login = ?

    // Check if a user with a specific ID exists
    boolean existsById(Long id);

    // Custom method to find users by competence (case insensitive)
    List<User> findByCompetenceIgnoreCase(String competence);
    // SELECT * FROM User WHERE LOWER(competence) = LOWER(?)
}
