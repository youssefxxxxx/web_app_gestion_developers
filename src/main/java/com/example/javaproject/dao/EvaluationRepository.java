package com.example.javaproject.dao;

import com.example.javaproject.entity.Evaluation;

import com.example.javaproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findAllByUser(User user); // Use the correct property name
}
