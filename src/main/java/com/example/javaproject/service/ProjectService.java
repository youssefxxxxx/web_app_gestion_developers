package com.example.javaproject.service;

import com.example.javaproject.dao.AssignmentRepository;
import com.example.javaproject.dao.ProjectRepository;
import com.example.javaproject.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public void createProject( Project project) {

        // Save the project to the database
        projectRepository.save(project);
    }

    public Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + projectId));
    }

    // Fetch all projects
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> getProjectsByDeveloper(Long userId) {
        return assignmentRepository.findProjectsByUserId(userId);
    }

    public void updateProject(Project updatedProject) {
        // Fetch the existing project from the database
        Project existingProject = projectRepository.findById(updatedProject.getIdP())
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + updatedProject.getIdP()));

        // Update project fields
        existingProject.setTitle(updatedProject.getTitle());
        existingProject.setDescription(updatedProject.getDescription());
        existingProject.setRequiredCompetence(updatedProject.getRequiredCompetence());
        existingProject.setEstimatedTime(updatedProject.getEstimatedTime());

        // Save the updated project back to the repository
        projectRepository.save(existingProject);
    }
}
