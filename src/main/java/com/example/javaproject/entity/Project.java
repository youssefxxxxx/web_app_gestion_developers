package com.example.javaproject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_p")
    private Long idP; // Project ID

    @Column(name = "title", nullable = false)
    private String title; // Title of the project

    @Column(name = "description", length = 1000)
    private String description; // Description of the project

    @Column(name = "required_competence", nullable = false)
    private String requiredCompetence; // Required competence for the project

    @Column(name = "estimated_time", nullable = false)
    private int estimatedTime; // Estimated time for project completion in hours or days

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    // Default Constructor
    public Project() {
    }

    // Constructor with all fields
    public Project(Long idP, String title, String description, String requiredCompetence, int estimatedTime, User createdBy) {
        this.idP = idP;
        this.title = title;
        this.description = description;
        this.requiredCompetence = requiredCompetence;
        this.estimatedTime = estimatedTime;
        this.createdBy = createdBy;
    }

    // Getters and Setters (if not using Lombok @Data for certain fields)
    public Long getIdP() {
        return idP;
    }

    public void setIdP(Long idP) {
        this.idP = idP;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequiredCompetence() {
        return requiredCompetence;
    }

    public void setRequiredCompetence(String requiredCompetence) {
        this.requiredCompetence = requiredCompetence;
    }

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "Project{" +
                "idP=" + idP +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", requiredCompetence='" + requiredCompetence + '\'' +
                ", estimatedTime=" + estimatedTime +
                ", createdBy=" + createdBy +
                '}';
    }
}
