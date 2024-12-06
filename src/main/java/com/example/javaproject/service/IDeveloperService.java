package com.example.javaproject.service;

import com.example.javaproject.entity.*;


import java.util.List;

public interface IDeveloperService {
    User updateAccount(Long userId, String oldPassword, String newPassword, String confirmPassword, String competence, int experience);
    List<Project> getProjectsAssignedToUser(Long userId);
    List<Evaluation> getEvaluationsForUser(Long userId);

}
