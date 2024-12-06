package com.example.javaproject.dao;

import com.example.javaproject.entity.Project;

import com.example.javaproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    // Use User type for createdBy field if it maps to a User object
    List<Project> findAllByCreatedBy(User createdBy);
}
