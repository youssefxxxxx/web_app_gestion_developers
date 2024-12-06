package com.example.javaproject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Evaluation")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_e")
    private Long idE; // Evaluation ID

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    private User user; // Use "user" as the property name

    @ManyToOne
    @JoinColumn(name = "id_project", nullable = false)
    private Project project; // Foreign key referencing the Project entity

    @Column(name = "note", nullable = false)
    private int note; // Note must be between 0 and 5

    @Column(name = "feedback", length = 1000)
    private String feedback; // Feedback as a VARCHAR

    // Default Constructor
    public Evaluation() {
    }

    // Constructor with all fields
    public Evaluation(Long idE, User user, Project project, int note, String feedback) {
        this.idE = idE;
        this.user = user;
        this.project = project;
        this.note = note;
        this.feedback = feedback;
    }

    // Getters and Setters (if not using Lombok @Data for specific fields)
    public Long getIdE() {
        return idE;
    }

    public void setIdE(Long idE) {
        this.idE = idE;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        if (note < 0 || note > 5) {
            throw new IllegalArgumentException("Note must be between 0 and 5.");
        }
        this.note = note;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "Evaluation{" +
                "idE=" + idE +
                ", user=" + user +
                ", project=" + project +
                ", note=" + note +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
