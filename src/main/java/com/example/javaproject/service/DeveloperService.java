package com.example.javaproject.service;

import com.example.javaproject.dao.UserRepository;
import com.example.javaproject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeveloperService implements IDeveloperService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Get a user by their ID.
     *
     * @param userId ID of the user to fetch.
     * @return User object.
     */
    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    /**
     * Update the account details of a user.
     *
     * @param userId           ID of the user to update.
     * @param newLogin         New login for the user.
     * @param oldPassword      User's old password.
     * @param newPassword      User's new password.
     * @param confirmPassword  Confirmation of the new password.
     * @param competence       User's updated competence.
     * @param experience       User's updated experience.
     */
    @Override
    public void updateAccount(Long userId, String newLogin, String oldPassword, String newPassword,
                              String confirmPassword, String competence, int experience) {
        // Fetch user from database
        User user = getUserById(userId);

        // Validate old password
        if (!user.getMotDePasse().equals(oldPassword)) {
            throw new RuntimeException("Old password is incorrect.");
        }

        // Update password if provided
        if (newPassword != null && !newPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                throw new RuntimeException("New passwords do not match.");
            }
            user.setMotDePasse(newPassword);
        }

        // Update login if it is different
        if (newLogin != null && !newLogin.isEmpty() && !newLogin.equals(user.getLogin())) {
            if (userRepository.existsByLogin(newLogin)) {
                throw new RuntimeException("Login is already taken.");
            }
            user.setLogin(newLogin);
        }

        // Update other user details
        user.setCompetence(competence);
        user.setExperience(experience);

        // Save updated user to the database
        userRepository.save(user);
    }

    /**
     * Find developers based on their competence and experience.
     *
     * @param competence Competence to search for.
     * @param experience Minimum experience required.
     * @return List of developers matching the criteria.
     */
    @Override
    public List<User> findDevelopers(String competence, int experience) {
        return userRepository.findDevelopersByCompetenceAndExperience(competence, experience);
    }

    /**
     * Get all developers (users with role 'dev').
     *
     * @return List of all developers.
     */
    @Override
    public List<User> getAllDevelopers() {
        return userRepository.findByRole("dev");
    }
}
