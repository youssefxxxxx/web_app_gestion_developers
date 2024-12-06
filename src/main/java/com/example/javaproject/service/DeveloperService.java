package com.example.javaproject.service;

import com.example.javaproject.dao.UserRepository;
import com.example.javaproject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeveloperService {

    @Autowired
    private UserRepository userRepository;

    // Method to get user by ID
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    // Method to update user account
    public void updateAccount(Long userId, String oldPassword, String newPassword, String confirmPassword, String competence, int experience) {
        // Fetch user from database
        User user = getUserById(userId);

        // Validate old password
        if (!user.getMotDePasse().equals(oldPassword)) {
            throw new RuntimeException("Old password is incorrect");
        }

        // Check if new passwords match
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("New passwords do not match");
        }

        // Update user details
        user.setMotDePasse(newPassword);
        user.setCompetence(competence);
        user.setExperience(experience);

        // Save the updated user
        userRepository.save(user);
    }
}
