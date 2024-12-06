package com.example.javaproject.controller;

import com.example.javaproject.entity.User;
import com.example.javaproject.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/developer")
public class DeveloperController {

    @Autowired
    private DeveloperService developerService;

    @GetMapping("/updateAccount")
    public String updateAccountPage(Model model, @RequestParam("userId") Long userId) {
        // Load user details by ID
        User user = developerService.getUserById(userId);
        model.addAttribute("user", user); // Bind the user object
        return "UpdateAccount"; // Return UpdateAccount.html
    }

    @PostMapping("/updateAccount")
    public String handleUpdateAccount(
            @RequestParam Long userId,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            @RequestParam String competence,
            @RequestParam int experience,
            Model model) {

        try {
            // Update the user account details
            developerService.updateAccount(userId, oldPassword, newPassword, confirmPassword, competence, experience);
            model.addAttribute("successMessage", "Account updated successfully!");
        } catch (RuntimeException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "UpdateAccount"; // If an error occurs, stay on the same page
        }

        return "redirect:/home"; // Redirect to developer's home page
    }

    @GetMapping("/home")
    public String developerHome() {
        return "DevHome"; // Return DevHome.html
    }
}
