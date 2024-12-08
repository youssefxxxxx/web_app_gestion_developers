package com.example.javaproject.service;

import com.example.javaproject.entity.User;

import java.util.List;

public interface IDeveloperService {

    /**
     * Get a user by their ID.
     *
     * @param userId ID of the user to fetch.
     * @return User object.
     */
    User getUserById(Long userId);

    /**
     * Update the account details of a user.
     *
     * @param userId ID of the user to update.
     * @param oldPassword User's old password.
     * @param newPassword User's new password.
     * @param confirmPassword Confirmation of the new password.
     * @param competence User's updated competence.
     * @param experience User's updated experience.
     */
    void updateAccount(Long userId, String oldPassword, String newPassword, String confirmPassword, String competence, int experience);

    /**
     * Find developers based on their competence and experience.
     *
     * @param competence Competence to search for.
     * @param experience Minimum experience required.
     * @return List of developers matching the criteria.
     */
    List<User> findDevelopers(String competence, int experience);
}
