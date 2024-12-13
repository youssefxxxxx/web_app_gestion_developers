package com.example.javaproject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Assignment", uniqueConstraints = {@UniqueConstraint(columnNames = {"id_projet", "id_user"})})
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_a")
    private Long idA; // Assignment ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projet", nullable = false)
    private Project project; // Foreign key referencing the Project entity

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private User user; // Foreign key referencing the User entity

    // Default Constructor
    public Assignment() {
    }

    // Constructor with all fields
    public Assignment(Project project, User user) {
        this.project = project;
        this.user = user;
    }

    // Getters and Setters
    public Long getIdA() {
        return idA;
    }

    public void setIdA(Long idA) {
        this.idA = idA;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Assignment{" +
                "idA=" + idA +
                ", project=" + project.getIdP() + // Avoid fetching full object to prevent lazy-loading issues
                ", user=" + user.getId() +
                '}';
    }
}
