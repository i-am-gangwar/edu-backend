package com.edu_backend.service;

import com.edu_backend.dto.QuestionDTO;
import com.edu_backend.model.QuizAttempt;
import com.edu_backend.model.QuizAttemptResult;
import com.edu_backend.repository.QuizAttemptRepository;
import com.edu_backend.repository.QuizAttemptResultAnalysisRepository;
import com.edu_backend.service.Interface.QuizAttemptResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class QuizAttemptResultServiceImpl implements QuizAttemptResultService {

    @Autowired
    QuizAttemptRepository quizAttemptRepository;

    @Autowired
   QuizAttemptResultAnalysisRepository quizAttemptResultAnalysisRepository;

    @Autowired
    QuestionService questionService;

    @Override
    public QuizAttemptResult quizAttemptResult(String userId) {

        // get result  for user if any
        Optional<QuizAttempt> submittedQuizAttempt  = quizAttemptRepository.findByUserId(userId);

        if (submittedQuizAttempt.isEmpty()) {
            throw new IllegalArgumentException("No results found for user: " + userId);
        }

        QuizAttemptResult quizAttemptResult = new QuizAttemptResult();
        quizAttemptResult.setUserId(userId);

        // for all quiz set attempts, calculate its results and save it
        for (QuizAttempt.QuizSet quizSet : submittedQuizAttempt.get().getQuizSet()) {

            for (QuizAttempt.QuizSetAttempt attempt : quizSet.getQuizSetAttempts()) {

                int attemptScore = calCulateQuizAttemptResult(attempt.getQuizSetAttempt());


            }
        }
//        resultAnalysis.setTotalScore(totalScore);
//        resultAnalysis.setHighScore(highScore);
//        resultAnalysis.setAverageScore(quizzesTaken == 0 ? 0 : (double) totalScore / quizzesTaken);
//        resultAnalysis.setSubjectWiseScores(subjectWiseScores);
//        resultAnalysis.setQuizzesTaken(quizzesTaken);
//        resultAnalysis.setLastUpdated(new Date());


        return null;
    }


// calculating quizAttempt result
    private int calCulateQuizAttemptResult(Map<String, List<String>> quizSetAttempt) {

        for (Map.Entry<String, List<String>> entry : quizSetAttempt.entrySet()) {
            String questionId = entry.getKey();
            List<String> questionAns = entry.getValue();
            System.out.println("key: " + questionId + ", value: " + questionAns);
            Optional<QuestionDTO> questionDTO = questionService.getQuestionById(questionId);
//            if(questionDTO.get().getOptions().get().isCorrect()=="true" &&
//            )


        }

        return  0;
    }

}