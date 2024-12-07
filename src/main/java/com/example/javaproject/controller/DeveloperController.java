package com.example.javaproject.controller;

import com.example.javaproject.entity.User;
import com.example.javaproject.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;


@Controller
public class DeveloperController {

    @Autowired
    private DeveloperService developerService;

    /**
     * Redirect to Update Account page with preloaded user data.
     */
    @GetMapping("/developer/updateAccount")
    public String updateAccountPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId"); // Retrieve userId from session
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if session expired
        }

        // Fetch user details and add to the model
        User user = developerService.getUserById(userId);
        model.addAttribute("user", user);

        return "UpdateAccount"; // Render UpdateAccount.html
    }

    /**
     * Handle Update Account form submission.
     */
    @PostMapping("/developer/updateAccount")
    public String handleUpdateAccount(
            HttpSession session,
            String oldPassword,
            String newPassword,
            String newConfirmedPassword,
            String competence,
            int experience,
            Model model) {

        Long userId = (Long) session.getAttribute("userId"); // Retrieve userId from session
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if session expired
        }

        try {
            // Update the user account
            developerService.updateAccount(userId, oldPassword, newPassword, newConfirmedPassword, competence, experience);

            // On success, invalidate the session and redirect to login
            session.invalidate();
            return "redirect:/login";
        } catch (RuntimeException ex) {
            // Handle errors and display them on the same page
            model.addAttribute("errorMessage", ex.getMessage());

            // Re-fetch user details to display them in the form
            User user = developerService.getUserById(userId);
            model.addAttribute("user", user);

            return "UpdateAccount"; // Stay on the UpdateAccount page
        }
    }

    /**
     * Developer Home Page.
     */
    @GetMapping("/developer/home")
    public String developerHome() {
        return "DevHome"; // Render DevHome.html
    }
}
