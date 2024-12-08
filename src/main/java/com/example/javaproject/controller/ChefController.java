package com.example.javaproject.controller;

import com.example.javaproject.entity.*;

import com.example.javaproject.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ChefController {
    @Autowired
    private EvaluationService evaluationService;
    @Autowired
    private ProjectService projectService;

    @Autowired
    private DeveloperService developerService;
    @Autowired
    private AssignmentService assignmentService;

    @Autowired
    private UserService userService;


    @GetMapping("/chef/home")
    public String ChefHome() {
        return "ChefHome"; // Render DevHome.html
    }
    @GetMapping("/chef/about")
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
        return "AboutChef"; // Render About.html
    }

    @GetMapping("/chef/aboutchef")
    public String showAboutChefPage(HttpSession session, Model model) {
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
        return "AboutChef"; // Render About.html
    }

    //for Updating account
    @GetMapping("/chef/updateAccount")
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
    @PostMapping("/chef/updateAccount")
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
    //for creating page
    // Display the Create Project page
    @GetMapping("/chef/create")
    public String showCreateProjectPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId"); // Check if the user is logged in
        String nameUser=(String) session.getAttribute("nameUser");
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if session expired
        }

        model.addAttribute("project", new Project()); // object that we should put in the <form
        return "CreateProject"; // Render CreateProject.html
    }
    // Handle the Create Project form submission
    @PostMapping("/chef/create")
    public String handleCreateProjectForm(
            @ModelAttribute("project") Project project,
            HttpSession session,
            Model model) {
        String nameUser=(String) session.getAttribute("nameUser");
        Long userId = (Long) session.getAttribute("userId"); // Check if the user is logged in
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if session expired
        }

        project.setCreatedBy(nameUser);
        System.out.println(project);

        try {
            // Call service to save the project
            projectService.createProject(project);

            // Redirect to a success or home page
            return "redirect:/chef/home";
        } catch (RuntimeException ex) {
            // Handle errors and display them on the same page
            model.addAttribute("errorMessage", ex.getMessage());
            return "CreateProject"; // Stay on the same page with error message
        }
    }
    //to assign

    @GetMapping("/chef/assignDeveloper")
    public String showAssignDeveloperPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if session expired
        }

        // Fetch all projects and developers to populate the dropdowns
        List<Project> projects = projectService.getAllProjects();
        List<User> developers = userService.getAllDevelopers();

        // Add data to the model for Thymeleaf
        model.addAttribute("projects", projects);
        model.addAttribute("users", developers);
        model.addAttribute("assignment", new Assignment()); // Empty Assignment object for the form

        return "AssignDeveloper"; // Render AssignDeveloper.html
    }
    @PostMapping("/chef/assignDeveloper")
    public String handleAssignDeveloperForm(
            @ModelAttribute("assignment") Assignment assignment,
            Model model) {
        try {
            // Save the assignment in the database
            assignmentService.assignDeveloperToProject(assignment);
            System.out.println(assignment);
            // Redirect to a success or home page
            return "redirect:/chef/home";
        } catch (RuntimeException ex) {
            // Handle errors and display them on the same page
            model.addAttribute("errorMessage", ex.getMessage());

            // Re-fetch projects and developers for the dropdowns
            List<Project> projects = projectService.getAllProjects();
            List<User> developers = userService.getAllDevelopers();
            model.addAttribute("projects", projects);
            model.addAttribute("users", developers);

            return "AssignDeveloper"; // Stay on the same page with an error message
        }
    }

    //find developer

    /**
     * Display the "Find Developer" page.
     */
    @GetMapping("/chef/findDeveloper")
    public String showFindDeveloperPage(Model model) {
        model.addAttribute("developers", null); // Initialize developers list as null
        return "FindDeveloper"; // Render the "FindDeveloper.html" page
    }

    /**
     * Handle the search for developers.
     */
    @PostMapping("/chef/findDeveloper")
    public String handleFindDeveloper(
            @RequestParam("competence") String competence,
            @RequestParam("experience") int experience,
            Model model) {
        try {
            // Call the service to find developers
            List<User> developers = developerService.findDevelopers(competence, experience);

            if (developers.isEmpty()) {
                model.addAttribute("errorMessage", "No developers found matching the criteria.");
            } else {
                model.addAttribute("developers", developers);
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while searching for developers: " + e.getMessage());
        }

        return "FindDeveloper"; // Stay on the same page and display the results
    }

    //Evaluate dev
    @GetMapping("/chef/evaluateDeveloper")
    public String showEvaluateDeveloperPage(Model model) {
        try {
            // Fetch only developers and all projects
            List<User> developers = userService.getAllDevelopers(); // Method to fetch only developers
            List<Project> projects = projectService.getAllProjects();

            // Add them to the model
            model.addAttribute("users", developers); // Pass developers to the view
            model.addAttribute("projects", projects);
            model.addAttribute("evaluation", new Evaluation()); // Bind an empty Evaluation object

            return "EvaluateDeveloper"; // Render the HTML page
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading data: " + e.getMessage());
            return "EvaluateDeveloper";
        }
    }



    @PostMapping("/chef/evaluateDeveloper")
    public String handleEvaluateDeveloper(
            @ModelAttribute("evaluation") Evaluation evaluation,
            @RequestParam("projectId") Long projectId,
            @RequestParam("userId") Long userId,
            Model model) {

        try {
            // Fetch the project and user
            Project project = projectService.getProjectById(projectId);
            User user = userService.getUserById(userId);

            if (project == null || user == null) {
                model.addAttribute("errorMessage", "Invalid project or user selected.");
                return "EvaluateDeveloper";
            }

            // Set the project and user in the evaluation object
            evaluation.setProject(project);
            evaluation.setUser(user);

            // Save the evaluation
            evaluationService.saveEvaluation(evaluation);

            model.addAttribute("successMessage", "Evaluation submitted successfully!");
            return "EvaluateDeveloper";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error saving evaluation: " + e.getMessage());
            return "EvaluateDeveloper";
        }
    }
}
