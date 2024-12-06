package com.example.javaproject.service;

import com.example.javaproject.dao.UserRepository;
import com.example.javaproject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository dao;

    @Override
    public User findByLoginAndPassword(String login, String password) {
        return dao.findByLoginAndMotDePasse(login, password);
    }


    public String registerUser(User user) {
        // Check if the login already exists
        if (dao.existsByLogin(user.getLogin())) {
            return "Login already exists. Please choose another one.";
        }

        // Validate if the passwords match
        if (!user.getMotDePasse().equals(user.getConfirmePassword())) {
            return "Passwords do not match.";
        }

        // Save the user to the database
        dao.save(user);

        return "User registered successfully!";
    }


}
