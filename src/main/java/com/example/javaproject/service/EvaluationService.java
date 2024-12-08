package com.example.javaproject.service;

import com.example.javaproject.dao.EvaluationRepository;
import com.example.javaproject.entity.Evaluation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EvaluationService implements IEvaluationService {
    @Autowired
    private EvaluationRepository evaluationRepository;

    public void saveEvaluation(Evaluation evaluation) {
        if (evaluation.getNote() < 0 || evaluation.getNote() > 5) {
            throw new IllegalArgumentException("Note must be between 0 and 5.");
        }

        evaluationRepository.save(evaluation);
    }
    public List<Evaluation> getEvaluationsByDeveloper(Long userId) {
        return evaluationRepository.findByUserId(userId);
    }
}
