package com.example.javaproject.dao;

import com.example.javaproject.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    boolean existsByProjectAndUser(Project project, User user);

    @Query("SELECT a.project FROM Assignment a WHERE a.user.id = :userId")
    List<Project> findProjectsByUserId(@Param("userId") Long userId);


}
