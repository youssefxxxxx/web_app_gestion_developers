package com.example.javaproject.service;

import com.example.javaproject.dao.AssignmentRepository;
import com.example.javaproject.dao.UserRepository;
import com.example.javaproject.entity.Assignment;
import com.example.javaproject.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service
public class AssignmentService {


    @Autowired
    private AssignmentRepository dao;

    public void assignDeveloperToProject(Assignment assignment) {
        // Check if the assignment already exists
        if (dao.existsByProjectAndUser(assignment.getProject(), assignment.getUser())) {
            throw new RuntimeException("This developer is already assigned to the selected project.");
        }

        // Save the assignment
        dao.save(assignment);
    }

}
