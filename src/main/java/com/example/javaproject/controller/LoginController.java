package com.example.javaproject.controller;

import com.example.javaproject.entity.User;
import com.example.javaproject.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;


@Controller
public class LoginController {

    @Autowired
    private IUserService userService;

    // Display the login page
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User()); // Add an empty User object for binding in the form
        return "Login3"; // Login3.html corresponds to the login form
    }

    // Handle login form submission
    @PostMapping("/login")
    public String handleLogin(
            @ModelAttribute("user") User user,
            Model model,
            HttpSession session) { // Add HttpSession to manage userId in session
        System.out.println("handleLogin");

        // Authenticate user
        User existingUser = userService.findByLoginAndPassword(user.getLogin(), user.getMotDePasse());
        if (existingUser != null) {
            session.setAttribute("userId", existingUser.getId()); // Store userId in the session
            session.setAttribute("role", existingUser.getRole()); // Optional: Store user role for authorization
            System.out.println(session.getAttribute("userId"));
            session.setAttribute("nameUser", existingUser.getNom()); // Optional: Store user role for authorization

            // Redirect based on role
            if ("dev".equalsIgnoreCase(existingUser.getRole())) {
                return "redirect:/developer/home"; // Redirect to DevHome.html
            } else if ("chef".equalsIgnoreCase(existingUser.getRole())) {
                return "redirect:/Chef"; // Redirect to ChefHome.html
            } else {
                model.addAttribute("error1", "Unknown role");
                return "Login3"; // Stay on login page with error message
            }
        } else {
            model.addAttribute("error", "Invalid login or password");
            return "Login3"; // Stay on login page with error message
        }
    }

    // Developer home page
    @GetMapping("/Dev")
    public String devPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if session expired
        }
        model.addAttribute("userId", userId); // Optional: Pass userId to the view if needed
        return "DevHome"; // DevHome.html for developers
    }

    // Chef home page
    @GetMapping("/Chef")
    public String chefPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if session expired
        }
        model.addAttribute("userId", userId); // Optional: Pass userId to the view if needed
        return "ChefHome"; // ChefHome.html for chefs
    }

    // Display registration page
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User()); // Add an empty User object for registration
        return "register"; // Corresponds to register.html
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session
        return "redirect:/login"; // Redirect to login page
    }

    // Handle registration form submission
    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("user") User user, Model model) {
        // Call the service to register the user
        String result = userService.registerUser(user);

        if (result.equals("User registered successfully!")) {
            return "redirect:/login"; // Redirect to login page on success
        } else {
            model.addAttribute("error2", result);
            return "register"; // Stay on registration page with error message
        }
    }
}
