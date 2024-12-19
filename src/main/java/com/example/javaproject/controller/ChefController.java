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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            String newLogin,
            String oldPassword,
            String newPassword,
            String newConfirmedPassword,
            String competence,
            int experience,
            @RequestParam("availability") boolean availability, // Added availability
            Model model) {

        Long userId = (Long) session.getAttribute("userId"); // Retrieve userId from session
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if session expired
        }

        try {
            // Update the user account
            developerService.updateAccount(
                    userId, newLogin, oldPassword, newPassword,
                    newConfirmedPassword, competence, experience, availability);

            // On success, invalidate the session and redirect to home
            session.invalidate();
            return "redirect:/chef/home";
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
    @GetMapping("/chef/createProject")
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
    @PostMapping("/chef/createProject")
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

    // Combined GetMapping for FindDeveloper and AssignDeveloper
    @GetMapping("/chef/findDeveloper")
    public String showFindAndAssignDeveloperPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login"; // Redirect to login if session expired
        }

        // Fetch all projects and initialize developers list as null
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects); // Add projects to the model
        model.addAttribute("developers", null);   // Initialize developers list as null
        model.addAttribute("assignment", new Assignment()); // Empty Assignment object for form

        return "FindDeveloper"; // Render the FindDeveloper.html page
    }

    // PostMapping for searching developers
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

        // Re-fetch all projects for the dropdown
        List<Project> projects = projectService.getAllProjects();
        model.addAttribute("projects", projects);

        return "FindDeveloper"; // Stay on the same page and display the results
    }

    @PostMapping("/chef/assignDeveloper")
    public String handleAssignDeveloperForm(
            @RequestParam("developerId") Long developerId,
            @RequestParam("projectId") Long projectId,
            Model model) {
        try {
            // Fetch the Project and User entities directly
            Project project = projectService.getProjectById(projectId);
            User developer = userService.getUserById(developerId);

            if (project == null || developer == null) {
                throw new RuntimeException("Invalid project or developer ID provided.");
            }

            // Check if the developer is already assigned to the project
            boolean isAlreadyAssigned = assignmentService.isDeveloperAssignedToProject(developerId, projectId);
            if (isAlreadyAssigned) {
                throw new RuntimeException("Developer is already assigned to this project.");
            }

            // Create and save the Assignment
            Assignment assignment = new Assignment();
            assignment.setProject(project);
            assignment.setUser(developer);

            assignmentService.assignDeveloperToProject(assignment);

            // Redirect to the home page after successful assignment
            return "redirect:/chef/home";
        } catch (RuntimeException ex) {
            // Handle errors and display them on the same page
            model.addAttribute("errorMessage", ex.getMessage());

            // Re-fetch projects and developers for the dropdowns
            List<Project> projects = projectService.getAllProjects();
            List<User> developers = userService.getAllDevelopers();
            model.addAttribute("projects", projects);
            model.addAttribute("developers", developers);

            return "FindDeveloper"; // Stay on the same page with an error message
        }
    }




    //Evaluate dev
    @GetMapping("/chef/evaluateDeveloper")
    public String showEvaluateDeveloperPage(
            @RequestParam(value = "userId", required = false) Long userId,
            Model model) {
        try {
            // Fetch all developers
            List<User> developers = userService.getAllDevelopers();
            model.addAttribute("users", developers);

            // Fetch projects assigned to the selected developer (if any)
            List<Project> projects = (userId != null)
                    ? assignmentService.getProjectsByDeveloper(userId)
                    : new ArrayList<>();
            model.addAttribute("projects", projects);

            // Add an empty evaluation object for the form binding
            model.addAttribute("evaluation", new Evaluation());
            model.addAttribute("selectedUserId", userId); // To retain selected developer
            System.out.println("les prjts");
            System.out.println(projects);
            System.out.println("userId:");
            System.out.println(userId);
            return "EvaluateDeveloper"; // Render the EvaluateDeveloper.html
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading evaluation page: " + e.getMessage());
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


            // Fetch the project and validate that the user is assigned to it
            Project project = projectService.getProjectById(projectId);
            if (project == null) {
                model.addAttribute("errorMessage", "Selected project does not exist.");
                return "EvaluateDeveloper";
            }

            if (!assignmentService.isDeveloperAssignedToProject(userId, projectId)) {
                model.addAttribute("errorMessage", "The developer is not assigned to the selected project.");
                return "EvaluateDeveloper";
            }

            // Fetch the user
            User user = userService.getUserById(userId);
            if (user == null) {
                model.addAttribute("errorMessage", "Selected developer does not exist.");
                return "EvaluateDeveloper";
            }


            // Set the project and user in the evaluation object
            evaluation.setProject(project);
            evaluation.setUser(user);

            // Save the evaluation
            evaluationService.saveEvaluation(evaluation);
            // Success message
            model.addAttribute("successMessage", "Evaluation submitted successfully!");
            return "redirect:/chef/home";
        } catch (Exception e) {
            // Debugging: Print exception details
            e.printStackTrace();
            model.addAttribute("errorMessage", "Error saving evaluation: " + e.getMessage());
            return "EvaluateDeveloper";
        }
    }

    // GetMapping for displaying the Create Developer page
    @GetMapping("/chef/createDeveloper")
    public String showCreateDeveloperPage(Model model) {
        // Add an empty User object to the model to bind with the form
        model.addAttribute("developer", new User());
        return "CreateDeveloper"; // Render CreateDeveloper.html
    }

    // PostMapping for handling the creation of a new developer
    @PostMapping("/chef/createDeveloper")
    public String handleCreateDeveloperForm(
            @ModelAttribute("developer") User developer,
            Model model) {
        try {
            System.out.println(developer);
            // Save the developer using the service
            developer.setRole("dev");
            userService.saveDeveloper(developer);

            // Redirect to the Developer CRUD or another success page
            return "redirect:/chef/home";
        } catch (Exception ex) {
            // Handle errors and display them on the same page
            model.addAttribute("errorMessage", "Error creating developer: " + ex.getMessage());
            return "CreateDeveloper"; // Stay on the same page with the error message
        }
    }

    @GetMapping("/chef/updateDeveloper")
    public String showUpdateDeveloperPage(Model model) {
        try {
            // Fetch all developers
            List<User> developers = userService.getAllDevelopers();
            model.addAttribute("developers", developers);

            return "UpdateDeveloper"; // Render the UpdateDeveloper.html template
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading developers: " + e.getMessage());
            return "UpdateDeveloper"; // Stay on the page in case of error
        }
    }

    @PostMapping("/chef/updateDeveloper")
    public String handleUpdateDeveloper(
            @RequestParam("developerId") Long developerId,
            @RequestParam(value = "competence", required = false) String competence,
            @RequestParam(value = "login", required = false) String login,
            @RequestParam(value = "experience", required = false) Integer experience,
            @RequestParam(value = "newPassword", required = false) String newPassword,
            Model model) {
        try {
            // Fetch the developer by ID
            User developer = userService.getUserById(developerId);
            if (developer == null) {
                model.addAttribute("errorMessage", "Developer not found.");
                return "UpdateDeveloper"; // Stay on the page in case of error
            }

            // Update the developer's competence and experience if provided
            if (competence != null && !competence.isEmpty()) {
                developer.setCompetence(competence);
            }
            if (experience != null) {
                developer.setExperience(experience);
            }
            if (login != null) {
                developer.setLogin(login);
            }

            // Update the developer's password if provided
            if (newPassword != null && !newPassword.isEmpty()) {
                developer.setMotDePasse(newPassword);
            }

            // Save the updated developer details
            userService.saveDeveloper(developer);

            model.addAttribute("successMessage", "Developer updated successfully.");
            // Re-fetch updated developers for the list
            List<User> developers = userService.getAllDevelopers();
            model.addAttribute("developers", developers);

            return "redirect:/chef/home"; // Redirect to the update page with updated list
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating developer: " + e.getMessage());
            return "UpdateDeveloper"; // Stay on the page in case of error
        }
    }

    @GetMapping("/chef/deleteDeveloper")
    public String showDeleteDeveloperPage(Model model) {
        // Fetch all developers with the role 'dev'
        List<User> developers = userService.getAllDevelopers();
        model.addAttribute("developers", developers); // Add developers to the model
        return "DeleteDeveloper"; // Render DeleteDeveloper.html
    }

    @PostMapping("/chef/deleteDeveloper")
    public String handleDeleteDeveloper(@RequestParam("developerId") Long developerId, Model model) {
        try {
            // Delete the developer
            userService.deleteDeveloper(developerId);

            // Redirect to the same page after successful deletion
            return "redirect:/chef/deleteDeveloper";
        } catch (RuntimeException e) {
            // Handle errors and display them on the same page
            model.addAttribute("errorMessage", e.getMessage());

            // Fetch developers again to populate the list
            List<User> developers = userService.getAllDevelopers();
            model.addAttribute("developers", developers);

            return "DeleteDeveloper"; // Stay on the same page with an error message
        }
    }
    @GetMapping("/chef/viewDevelopers")
    public String viewDevelopers(Model model) {
        try {
            List<User> developers = userService.getAllDevelopers();
            System.out.println(developers);
            model.addAttribute("developers", developers);
            return "ViewDevelopers";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading developers: " + e.getMessage());
            return "ViewDevelopers";
        }
    }

    @GetMapping("/chef/updateProject")
    public String showUpdateProjectPage(Model model) {
        try {
            // Fetch all projects
            List<Project> projects = projectService.getAllProjects();
            model.addAttribute("projects", projects);

            // Add an empty project object for the form binding
            model.addAttribute("project", new Project());

            return "UpdateProject"; // Render UpdateProject.html
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading the page: " + e.getMessage());
            return "UpdateProject"; // Show the same page with error
        }
    }
    @PostMapping("/chef/updateProject")
    public String handleUpdateProject(
            @ModelAttribute("project") Project updatedProject,
            @RequestParam("projectId") Long projectId,
            Model model) {
        try {
            // Fetch the existing project from the database
            Project existingProject = projectService.getProjectById(projectId);

            if (existingProject == null) {
                model.addAttribute("errorMessage", "The selected project does not exist.");
                return showUpdateProjectPage(model); // Reload the page with an error
            }

            // Update the project's details
            existingProject.setTitle(updatedProject.getTitle());
            existingProject.setDescription(updatedProject.getDescription());
            existingProject.setRequiredCompetence(updatedProject.getRequiredCompetence());
            existingProject.setEstimatedTime(updatedProject.getEstimatedTime());

            // Save the updated project
            projectService.updateProject(existingProject);

            model.addAttribute("successMessage", "Project updated successfully!");
            return "redirect:/chef/updateProject"; // Redirect to the same page
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating the project: " + e.getMessage());
            return showUpdateProjectPage(model); // Reload the page with an error
        }
    }
    @GetMapping("/chef/viewProjects")
    public String viewProjectsPage(Model model) {
        try {
            // Fetch all projects
            List<Project> projects = projectService.getAllProjects();
            model.addAttribute("projects", projects);
            return "ViewProjects"; // Render the ViewProjects.html
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error loading projects: " + e.getMessage());
            return "ViewProjects"; // Render the same page with an error
        }
    }



}
