package com.example.javaproject.controller;

import com.example.javaproject.entity.Evaluation;
import com.example.javaproject.entity.Project;
import com.example.javaproject.entity.User;
import com.example.javaproject.service.AssignmentService;
import com.example.javaproject.service.DeveloperService;
import com.example.javaproject.service.EvaluationService;
import com.example.javaproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpSession;

import java.util.List;


@Controller
public class DeveloperController {
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private DeveloperService developerService;
    @Autowired
    private UserService userService;
    @Autowired
    private EvaluationService evaluationService;
    /**
     * Redirect to Update Account page with preloaded user data.
     */

    /**
     * Developer Home Page.
     */
    @GetMapping("/developer/home")
    public String developerHome(HttpSession session, Model model) {
        // Retrieve the user ID from the session
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/login"; // Redirect to login if session expired
        }

        // Fetch the user from the database
        User user = userService.getUserById(userId);
        System.out.println("user.getNom()");
        System.out.println(user.getNom());
        // Add the user's name to the model
        model.addAttribute("nameUser", user.getNom());

        return "DevHome"; // Render DevHome.html
    }




    @GetMapping("/developer/about")
    public String showAboutPage(HttpSession session, Model model) {
        // Check if the user is logged in by validating the session
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if the session is invalid
        }

        // Fetch user information from the database using the userId
        User user = userService.getUserById(userId);
        if (user == null) {
            model.addAttribute("errorMessage", "User not found!");
            return "redirect:/login"; // Return About.html with an error message
        }

        // Pass the user information to the Thymeleaf page
        model.addAttribute("user", user);
        return "About"; // Render About.html
    }

    @GetMapping("/developer/currentProjects")
    public String showAssignedProjects(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId"); // Retrieve user ID from session
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if the session is invalid
        }

        try {
            // Fetch the projects assigned to the developer
            List<Project> projects = assignmentService.getProjectsByDeveloper(userId);
            model.addAttribute("projects", projects);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Unable to fetch assigned projects. Please try again.");
        }

        return "ConsultProject"; // Render the ConsultProject.html page
    }



//
    //evaluation
    @GetMapping("/developer/evaluations")
    public String showEvaluations(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId"); // Retrieve user ID from session
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if the session is invalid
        }

        // Fetch evaluations for the developer
        List<Evaluation> evaluations = evaluationService.getEvaluationsByDeveloper(userId);
        model.addAttribute("evaluations", evaluations);

        return "ConsultEvaluation"; // Render the ConsultEvaluations.html page
    }


    //for Updating account
    @GetMapping("/developer/updateAccount")
    public String updateAccountPage1(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId"); // Retrieve userId from session
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if session expired
        }
        // Fetch user details and add to the model
        User user = developerService.getUserById(userId);
        model.addAttribute("user", user);

        return "UpdateAccountDev"; // Render UpdateAccount.html
    }

    /**
     * Handle Update Account form submission.
     */
    @PostMapping("/developer/updateAccount")
    public String handleUpdateAccount1(
            HttpSession session,
            String newLogin,
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
            developerService.updateAccount(userId,newLogin,oldPassword, newPassword, newConfirmedPassword, competence, experience);

            // On success, invalidate the session and redirect to login
            session.invalidate();
            return "redirect:/developer/home";
        } catch (RuntimeException ex) {
            // Handle errors and display them on the same page
            model.addAttribute("errorMessage", ex.getMessage());

            // Re-fetch user details to display them in the form
            User user = developerService.getUserById(userId);
            model.addAttribute("user", user);

            return "UpdateAccountDev"; // Stay on the UpdateAccount page
        }
    }
}
