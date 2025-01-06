package com.edu_backend.service;

import com.edu_backend.dto.QuestionDTO;
import com.edu_backend.model.QuizAttempt;
import com.edu_backend.model.QuizAttemptResultAnalysis;
import com.edu_backend.repository.QuizAttemptRepository;
import com.edu_backend.repository.QuizAttemptResultAnalysisRepository;
import com.edu_backend.service.Interface.QuizAttemptResultAnalysisService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
@AllArgsConstructor
public class QuizAttemptResultAnalysisServiceImpl implements QuizAttemptResultAnalysisService {

    @Override
    public QuizAttemptResultAnalysis QuizAttemptResult(String userId) {
        return null;
    }
}