package com.example.javaproject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "mot_de_passe", nullable = false)
    private String motDePasse;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "experience")
    private int experience;

    @Column(name = "dispo", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private boolean disponibilite = true;

    @Column(name = "competence", length = 255)
    private String competence; // New field for competence
    @Transient // Excluded from persistence, used for form validation only
    private String confirmePassword;

    // Default constructor
    public User() {
        this.disponibilite = true; // Default value
    }

    public User(Long id, String nom, String login, String motDePasse, String role, int experience, boolean disponibilite, String competence, String confirmePassword) {
        this.id = id;
        this.nom = nom;
        this.login = login;
        this.motDePasse = motDePasse;
        this.role = role;
        this.experience = experience;
        this.disponibilite = disponibilite;
        this.competence = competence;
        this.confirmePassword = confirmePassword;
    }

    public String getCompetence() {
        return competence;
    }

    public void setCompetence(String competence) {
        this.competence = competence;
    }

    // Getters and Setters (if not using Lombok @Data for some fields)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public boolean isDisponibilite() {
        return disponibilite;
    }

    public void setDisponibilite(boolean disponibilite) {
        this.disponibilite = disponibilite;
    }

    public String getConfirmePassword() {
        return confirmePassword;
    }

    public void setConfirmePassword(String confirmePassword) {
        this.confirmePassword = confirmePassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", login='" + login + '\'' +
                ", motDePasse='" + motDePasse + '\'' +
                ", role='" + role + '\'' +
                ", experience=" + experience +
                ", disponibilite=" + disponibilite +
                ", competence='" + competence + '\'' +
                ", confirmePassword='" + confirmePassword + '\'' +
                '}';
    }
}
