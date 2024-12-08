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


    @Column(name = "created_by", nullable = false)
    private String createdBy;

    // Default Constructor
    public Project() {
    }

    // Constructor with all fields


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

    public int getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getRequiredCompetence() {
        return requiredCompetence;
    }

    public void setRequiredCompetence(String requiredCompetence) {
        this.requiredCompetence = requiredCompetence;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
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
                ", createdBy='" + createdBy + '\'' +
                '}';
    }
}
