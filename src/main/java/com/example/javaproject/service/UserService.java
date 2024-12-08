package com.example.javaproject.service;

import com.example.javaproject.dao.UserRepository;
import com.example.javaproject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    @Override
    public User getUserById(Long userId) {
        // Find the user by ID, and return null if not found
        Optional<User> userOptional = dao.findById(userId);
        return userOptional.orElse(null); // Return null if the user is not found
    }

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllDevelopers() {
        return userRepository.findByRole("dev"); // Fetch only users with the "dev" role
    }


}
